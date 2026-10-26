/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.php.impl.codeInsight.controlFlow;

import com.jetbrains.php.codeInsight.PhpScopeHolder;
import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlow;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpAccessInstruction.Access;
import com.jetbrains.php.codeInsight.controlFlow.instructions.PhpInstruction;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.util.CachedValueProvider;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.controlFlow.ControlFlow;
import consulo.language.controlFlow.ControlFlowBuilder;
import consulo.language.controlFlow.Instruction;
import consulo.language.impl.ast.LeafElement;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiModificationTracker;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.LanguageCachedValueUtil;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.psi.PhpStubElements;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Builds the control flow of a {@link PhpScopeHolder} with the platform {@link ControlFlowBuilder}, following the PSI shape of the PHP
 * parser. Works on the PSI of the file alone (no index, no resolve), so stub indexers may use it.
 * <p>
 * Instructions come in evaluation order: arguments before their call, the assigned value before the written variable. Branches
 * ({@code if}, ternaries, {@code ?:}, {@code ??}, {@code &&}, {@code ||}, {@code match}, {@code switch}) and loops get edges;
 * {@code return}, {@code throw} and {@code exit} go to the exit point. Nested functions, closures and classes are separate scopes:
 * of a closure only the variables of its {@code use} list are read here.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public final class PhpControlFlowBuilder {
    /**
     * Scopes of their own, never entered
     */
    private static final TokenSet NESTED_SCOPES = TokenSet.create(PhpElementTypes.FUNCTION, PhpElementTypes.CLASS,
        PhpStubElements.CLASS_METHOD, PhpElementTypes.NAMESPACE);

    private static final TokenSet LOOPS = TokenSet.create(PhpElementTypes.WHILE, PhpElementTypes.DO_WHILE, PhpElementTypes.FOR,
        PhpElementTypes.FOREACH);

    private static final TokenSet SHORT_CIRCUIT = TokenSet.create(PhpElementTypes.LOGICAL_EXPRESSION,
        PhpElementTypes.LITERAL_LOGICAL_EXPRESSION, PhpElementTypes.COALESCE_EXPRESSION);

    /**
     * Platform builder whose generic nodes are PHP instructions
     */
    private static final class Builder extends ControlFlowBuilder {
        @Override
        public Instruction startNode(@Nullable PsiElement element) {
            Instruction instruction = new PhpInstructionImpl(this, element);
            addNodeAndCheckPending(instruction);
            return instruction;
        }
    }

    private final Builder myBuilder = new Builder();
    /**
     * Statements {@code break}/{@code continue} can leave: loops and {@code switch}, innermost last
     */
    private final Deque<PsiElement> myBreakTargets = new ArrayDeque<>();

    private PhpControlFlowBuilder() {
    }

    @RequiredReadAction
    public static PhpControlFlow getControlFlow(PhpScopeHolder scopeHolder) {
        return LanguageCachedValueUtil.getCachedValue(scopeHolder,
            () -> CachedValueProvider.Result.create(new PhpControlFlowBuilder().build(scopeHolder), PsiModificationTracker.MODIFICATION_COUNT));
    }

    @RequiredReadAction
    private PhpControlFlow build(PhpScopeHolder scopeHolder) {
        // entry point
        myBuilder.startNode(null);
        for (PsiElement element : getScopeElements(scopeHolder)) {
            visit(element);
        }
        // exit point: every pending edge ends here
        Instruction exit = myBuilder.startNode(null);
        myBuilder.checkPending(exit);

        ControlFlow controlFlow = myBuilder.completeControlFlow();
        Instruction[] instructions = controlFlow.getInstructions();
        PhpInstruction[] result = new PhpInstruction[instructions.length];
        for (int i = 0; i < instructions.length; i++) {
            result[i] = (PhpInstruction) instructions[i];
        }
        return new PhpControlFlowImpl(result);
    }

    /**
     * Elements of the scope in order: the body of a function, the statements of a namespace or file
     */
    @RequiredReadAction
    private static List<PsiElement> getScopeElements(PhpScopeHolder scopeHolder) {
        if (scopeHolder instanceof PhpNamespace namespace) {
            PsiElement statements = namespace.getStatements();
            return statements == null ? List.of() : List.of(statements);
        }
        if (scopeHolder instanceof PhpFile) {
            return children(scopeHolder);
        }
        if (scopeHolder.getNode().getElementType() == PhpElementTypes.ANONYMOUS_FUNCTION && isArrowFunction(scopeHolder)) {
            // fn($x) => expression
            PsiElement arrow = findChildByType(scopeHolder, PhpTokenTypes.HASH_ARRAY);
            PsiElement body = arrow == null ? null : nextComposite(arrow);
            return body == null ? List.of() : List.of(body);
        }
        PsiElement body = lastChildOfType(scopeHolder, PhpElementTypes.GROUP_STATEMENT);
        return body == null ? List.of() : List.of(body);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // dispatch

    @RequiredReadAction
    private void visit(PsiElement element) {
        if (isLeaf(element)) {
            return;
        }
        IElementType type = element.getNode().getElementType();
        if (NESTED_SCOPES.contains(type) || type == PhpDocElementTypes.DOC_COMMENT) {
            return;
        }
        if (type == PhpElementTypes.ANONYMOUS_FUNCTION) {
            visitClosure(element);
        }
        else if (type == PhpElementTypes.ANONYMOUS_CLASS) {
            // only the constructor arguments are evaluated here
            visitChildrenOfType(element, PhpElementTypes.PARAMETER_LIST);
        }
        else if (type == PhpElementTypes.VARIABLE_REFERENCE) {
            visitVariable(element, Access.READ);
        }
        else if (type == PhpElementTypes.ASSIGNMENT_EXPRESSION) {
            visitAssignment(element);
        }
        else if (type == PhpElementTypes.MULTIASSIGNMENT_EXPRESSION) {
            visitMultiAssignment(element);
        }
        else if (type == PhpElementTypes.SELF_ASSIGNMENT_EXPRESSION) {
            visitSelfAssignment(element);
        }
        else if (type == PhpElementTypes.FUNCTION_REFERENCE || type == PhpElementTypes.METHOD_REFERENCE) {
            visitChildren(element);
            if (element instanceof FunctionReference reference) {
                myBuilder.addNodeAndCheckPending(new PhpInstructions.Call(myBuilder, reference));
            }
        }
        else if (type == PhpElementTypes.NEW_EXPRESSION) {
            visitChildren(element);
            if (element instanceof NewExpression expression) {
                myBuilder.addNodeAndCheckPending(new PhpInstructions.ConstructorCall(myBuilder, expression));
            }
        }
        else if (type == PhpElementTypes.RETURN) {
            PsiElement argument = firstComposite(element);
            visitChildren(element);
            myBuilder.addNodeAndCheckPending(new PhpInstructions.Return(myBuilder, element, argument));
            abruptFlow(element);
        }
        else if (type == PhpElementTypes.THROW || type == PhpElementTypes.THROW_EXPRESSION) {
            PsiElement argument = firstComposite(element);
            visitChildren(element);
            myBuilder.addNodeAndCheckPending(new PhpInstructions.Throw(myBuilder, element, argument));
            abruptFlow(element);
        }
        else if (type == PhpElementTypes.EXIT_EXPRESSION) {
            visitChildren(element);
            myBuilder.startNode(element);
            abruptFlow(element);
        }
        else if (type == PhpElementTypes.YIELD) {
            PsiElement argument = lastComposite(element);
            visitChildren(element);
            myBuilder.addNodeAndCheckPending(new PhpInstructions.Yield(myBuilder, element, argument, isYieldFrom(element)));
        }
        else if (type == PhpElementTypes.IF) {
            visitIf(element);
        }
        else if (type == PhpElementTypes.WHILE) {
            visitWhile(element);
        }
        else if (type == PhpElementTypes.DO_WHILE) {
            visitDoWhile(element);
        }
        else if (type == PhpElementTypes.FOR) {
            visitFor(element);
        }
        else if (type == PhpElementTypes.FOREACH) {
            visitForeach(element);
        }
        else if (type == PhpElementTypes.SWITCH) {
            visitSwitch(element);
        }
        else if (type == PhpElementTypes.TRY) {
            visitTry(element);
        }
        else if (type == PhpElementTypes.BREAK) {
            visitBreak(element);
        }
        else if (type == PhpElementTypes.CONTINUE) {
            visitContinue(element);
        }
        else if (type == PhpElementTypes.TERNARY_EXPRESSION || type == PhpElementTypes.ELVIS_EXPRESSION) {
            visitConditional(element);
        }
        else if (SHORT_CIRCUIT.contains(type)) {
            visitShortCircuit(element);
        }
        else if (type == PhpElementTypes.MATCH_EXPRESSION) {
            visitMatch(element);
        }
        else if (type == PhpElementTypes.GLOBAL || type == PhpElementTypes.STATIC) {
            // global $a; static $a = 1;
            visitDeclaredVariables(element);
        }
        else {
            visitChildren(element);
        }
    }

    @RequiredReadAction
    private void visitChildren(PsiElement element) {
        for (PsiElement child : children(element)) {
            visit(child);
        }
    }

    @RequiredReadAction
    private void visitChildrenOfType(PsiElement element, IElementType type) {
        for (PsiElement child : children(element)) {
            if (child.getNode().getElementType() == type) {
                visit(child);
            }
        }
    }

    /**
     * Return, throw and exit: the flow goes to the exit point, also from branches pending inside the element
     */
    private void abruptFlow(PsiElement element) {
        myBuilder.processPending((pendingScope, instruction) -> {
            if (pendingScope != null && PsiTreeUtil.isAncestor(element, pendingScope, false)) {
                myBuilder.addPendingEdge(null, instruction);
            }
            else {
                myBuilder.addPendingEdge(pendingScope, instruction);
            }
        });
        myBuilder.addPendingEdge(null, myBuilder.prevInstruction);
        myBuilder.flowAbrupted();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // variables

    @RequiredReadAction
    private void visitVariable(PsiElement element, Access access) {
        String name = element instanceof Variable variable ? variable.getName() : null;
        if (name == null || name.isEmpty()) {
            // $$name, ${expression}: the name is computed
            visitChildren(element);
            return;
        }
        myBuilder.addNodeAndCheckPending(new PhpInstructions.AccessVariable(myBuilder, (Variable) element, name, access));
    }

    /**
     * Writes into a target: a variable, an array element ({@code $a['x'] = }), a destructuring list ({@code [$a, $b] = }),
     * or anything else, whose parts are read
     */
    @RequiredReadAction
    private void visitWriteTarget(PsiElement target, Access access) {
        IElementType type = target.getNode().getElementType();
        if (type == PhpElementTypes.VARIABLE_REFERENCE) {
            visitVariable(target, access);
        }
        else if (type == PhpElementTypes.ARRAY_ACCESS_EXPRESSION) {
            // $a['x'] = ...: indexes are read, the array is written
            List<PsiElement> parts = children(target);
            for (int i = 1; i < parts.size(); i++) {
                visit(parts.get(i));
            }
            if (!parts.isEmpty()) {
                visitWriteTarget(parts.get(0), Access.WRITE);
            }
        }
        else if (type == PhpElementTypes.ARRAY_CREATION_EXPRESSION || type == PhpElementTypes.ARRAY_VALUE
            || type == PhpElementTypes.ARRAY_HASH || type == PhpElementTypes.PHP_ARRAY_VALUE) {
            // [$a, 'k' => $b] = ...; list($a, $b) = ...
            List<PsiElement> parts = children(target);
            for (int i = 0; i < parts.size(); i++) {
                PsiElement part = parts.get(i);
                IElementType partType = part.getNode().getElementType();
                if (partType == PhpElementTypes.ARRAY_KEY) {
                    visit(part);
                }
                else {
                    visitWriteTarget(part, hasReferenceMark(part) ? Access.WRITE_REF : Access.WRITE);
                }
            }
        }
        else {
            visit(target);
        }
    }

    @RequiredReadAction
    private void visitAssignment(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.size() < 2) {
            visitChildren(element);
            return;
        }
        // $b = &$a: $a is read and $b written by reference
        boolean byReference = findChildByType(element, PhpTokenTypes.opBIT_AND) != null;
        PsiElement value = parts.get(parts.size() - 1);
        if (byReference && value.getNode().getElementType() == PhpElementTypes.VARIABLE_REFERENCE) {
            visitVariable(value, Access.READ_REF);
        }
        else {
            visit(value);
        }
        visitWriteTarget(parts.get(0), byReference ? Access.WRITE_REF : Access.WRITE);
    }

    /**
     * [$a, $b] = $value; list($a, 'k' => $b) = $value: the value, then every target is written
     */
    @RequiredReadAction
    private void visitMultiAssignment(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.size() < 2) {
            visitChildren(element);
            return;
        }
        visit(parts.get(parts.size() - 1));
        for (int i = 0; i < parts.size() - 1; i++) {
            visitWriteTarget(parts.get(i), Access.WRITE);
        }
    }

    /**
     * $a .= 'x': the value, then the variable is read and written
     */
    @RequiredReadAction
    private void visitSelfAssignment(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.size() < 2) {
            visitChildren(element);
            return;
        }
        visit(parts.get(parts.size() - 1));
        PsiElement target = parts.get(0);
        if (target.getNode().getElementType() == PhpElementTypes.VARIABLE_REFERENCE) {
            visitVariable(target, Access.READ);
            visitVariable(target, Access.WRITE);
        }
        else {
            visitWriteTarget(target, Access.WRITE);
        }
    }

    @RequiredReadAction
    private void visitDeclaredVariables(PsiElement element) {
        for (PsiElement child : children(element)) {
            if (child.getNode().getElementType() == PhpElementTypes.VARIABLE_REFERENCE) {
                visitVariable(child, Access.WRITE);
            }
            else {
                visit(child);
            }
        }
    }

    /**
     * A closure is a scope of its own; here only its {@code use} list is read: {@code function () use ($a, &$b)}
     */
    @RequiredReadAction
    private void visitClosure(PsiElement element) {
        PsiElement uses = findChildByType(element, PhpElementTypes.CLOSURE_USES);
        if (uses == null) {
            return;
        }
        for (PsiElement child : children(uses)) {
            if (child.getNode().getElementType() == PhpElementTypes.VARIABLE_REFERENCE) {
                boolean byReference = isAfterReferenceMark(child);
                visitVariable(child, byReference ? Access.READ_REF : Access.READ);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // branches

    /**
     * if (a) {} elseif (b) {} else {}: [condition, body, ELSE_IF..., ELSE]
     */
    @RequiredReadAction
    private void visitIf(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.isEmpty()) {
            return;
        }
        visit(parts.get(0));
        Instruction branch = myBuilder.prevInstruction;
        boolean hasElse = false;
        for (int i = 1; i < parts.size(); i++) {
            PsiElement part = parts.get(i);
            IElementType type = part.getNode().getElementType();
            if (type == PhpElementTypes.ELSE_IF) {
                myBuilder.prevInstruction = branch;
                List<PsiElement> elseIf = children(part);
                if (!elseIf.isEmpty()) {
                    visit(elseIf.get(0));
                }
                branch = myBuilder.prevInstruction;
                for (int j = 1; j < elseIf.size(); j++) {
                    visit(elseIf.get(j));
                }
            }
            else if (type == PhpElementTypes.ELSE) {
                hasElse = true;
                myBuilder.prevInstruction = branch;
                visitChildren(part);
            }
            else {
                // then
                visit(part);
            }
            myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        }
        if (!hasElse) {
            myBuilder.addPendingEdge(element, branch);
        }
        myBuilder.flowAbrupted();
    }

    /**
     * cond ? a : b, cond ?: b: [condition, true part?, false part]
     */
    @RequiredReadAction
    private void visitConditional(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.size() < 2) {
            visitChildren(element);
            return;
        }
        visit(parts.get(0));
        Instruction branch = myBuilder.prevInstruction;
        for (int i = 1; i < parts.size(); i++) {
            myBuilder.prevInstruction = branch;
            visit(parts.get(i));
            myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        }
        // ?: yields the condition itself when it is truthy
        if (element.getNode().getElementType() == PhpElementTypes.ELVIS_EXPRESSION) {
            myBuilder.addPendingEdge(element, branch);
        }
        myBuilder.flowAbrupted();
    }

    /**
     * a && b, a || b, a ?? b: the right side may be skipped
     */
    @RequiredReadAction
    private void visitShortCircuit(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.size() < 2) {
            visitChildren(element);
            return;
        }
        visit(parts.get(0));
        myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        for (int i = 1; i < parts.size(); i++) {
            visit(parts.get(i));
        }
        myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        myBuilder.flowAbrupted();
    }

    /**
     * match (subject) { a, b => x, default => y }
     */
    @RequiredReadAction
    private void visitMatch(PsiElement element) {
        Instruction dispatch = null;
        boolean subjectVisited = false;
        for (PsiElement part : children(element)) {
            if (part.getNode().getElementType() != PhpElementTypes.MATCH_ARM) {
                visit(part);
                continue;
            }
            if (!subjectVisited) {
                dispatch = myBuilder.prevInstruction;
                subjectVisited = true;
            }
            myBuilder.prevInstruction = dispatch;
            visitChildren(part);
            myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        }
        myBuilder.flowAbrupted();
    }

    /**
     * switch (subject) { case a: ...; default: ... }: cases fall through into the next one
     */
    @RequiredReadAction
    private void visitSwitch(PsiElement element) {
        myBreakTargets.push(element);
        Instruction dispatch = null;
        boolean hasDefault = false;
        boolean inCases = false;
        for (PsiElement part : children(element)) {
            IElementType type = part.getNode().getElementType();
            if (type != PhpElementTypes.CASE && type != PhpElementTypes.CASE_DEFAULT) {
                visit(part);
                continue;
            }
            if (!inCases) {
                dispatch = myBuilder.prevInstruction;
                myBuilder.flowAbrupted();
                inCases = true;
            }
            hasDefault |= type == PhpElementTypes.CASE_DEFAULT;
            // entered from the dispatch or by falling through the previous case
            Instruction entry = myBuilder.startNode(part);
            myBuilder.addEdge(dispatch, entry);
            visitChildren(part);
        }
        myBreakTargets.pop();
        myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        if (!hasDefault) {
            myBuilder.addPendingEdge(element, inCases ? dispatch : myBuilder.prevInstruction);
        }
        myBuilder.flowAbrupted();
    }

    /**
     * try {} catch (E $e) {} finally {}: [body, CATCH..., FINALLY_STATEMENT?]
     */
    @RequiredReadAction
    private void visitTry(PsiElement element) {
        Instruction start = myBuilder.startNode(element);
        List<Instruction> ends = new ArrayList<>();
        PsiElement finallyStatement = null;
        for (PsiElement part : children(element)) {
            IElementType type = part.getNode().getElementType();
            if (type == PhpElementTypes.CATCH) {
                // any instruction of the body may throw: approximated by the start of the try
                myBuilder.prevInstruction = start;
                for (PsiElement catchPart : children(part)) {
                    if (catchPart.getNode().getElementType() == PhpElementTypes.VARIABLE_REFERENCE) {
                        visitVariable(catchPart, Access.WRITE);
                    }
                    else if (catchPart.getNode().getElementType() != PhpElementTypes.CLASS_REFERENCE) {
                        visit(catchPart);
                    }
                }
                ends.add(myBuilder.prevInstruction);
            }
            else if (type == PhpElementTypes.FINALLY_STATEMENT) {
                finallyStatement = part;
            }
            else {
                visit(part);
                ends.add(myBuilder.prevInstruction);
            }
        }
        if (finallyStatement == null) {
            for (Instruction end : ends) {
                myBuilder.addPendingEdge(element, end);
            }
            myBuilder.flowAbrupted();
            return;
        }
        myBuilder.flowAbrupted();
        Instruction finallyStart = myBuilder.startNode(finallyStatement);
        for (Instruction end : ends) {
            myBuilder.addEdge(end, finallyStart);
        }
        visitChildren(finallyStatement);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // loops

    /**
     * while (condition) body
     */
    @RequiredReadAction
    private void visitWhile(PsiElement element) {
        List<PsiElement> parts = children(element);
        Instruction head = myBuilder.startNode(element);
        if (!parts.isEmpty()) {
            visit(parts.get(0));
        }
        myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        visitLoopBody(element, parts.subList(Math.min(1, parts.size()), parts.size()), head);
    }

    /**
     * do body while (condition);
     */
    @RequiredReadAction
    private void visitDoWhile(PsiElement element) {
        List<PsiElement> parts = children(element);
        Instruction head = myBuilder.startNode(element);
        myBreakTargets.push(element);
        for (PsiElement part : parts) {
            visit(part);
        }
        myBreakTargets.pop();
        myBuilder.addEdge(myBuilder.prevInstruction, head);
    }

    /**
     * for (init; condition; step) body
     */
    @RequiredReadAction
    private void visitFor(PsiElement element) {
        // the three expression groups are split by ';', the body follows ')'
        List<List<PsiElement>> groups = new ArrayList<>(List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        List<PsiElement> body = new ArrayList<>();
        int group = 0;
        boolean inBody = false;
        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            IElementType type = child.getNode().getElementType();
            if (!inBody && type == PhpTokenTypes.opSEMICOLON && group < 2) {
                group++;
            }
            else if (!inBody && type == PhpTokenTypes.RPAREN) {
                inBody = true;
            }
            else if (!isLeaf(child)) {
                (inBody ? body : groups.get(group)).add(child);
            }
        }
        for (PsiElement init : groups.get(0)) {
            visit(init);
        }
        Instruction head = myBuilder.startNode(element);
        for (PsiElement condition : groups.get(1)) {
            visit(condition);
        }
        myBuilder.addPendingEdge(element, myBuilder.prevInstruction);
        List<PsiElement> bodyAndStep = new ArrayList<>(body);
        bodyAndStep.addAll(groups.get(2));
        visitLoopBody(element, bodyAndStep, head);
    }

    /**
     * foreach (array as $key => $value) body: [array, IS_REFERENCE, target, (IS_REFERENCE, target)?, body]
     */
    @RequiredReadAction
    private void visitForeach(PsiElement element) {
        List<PsiElement> parts = children(element);
        if (parts.isEmpty()) {
            return;
        }
        visit(parts.get(0));
        Instruction head = myBuilder.startNode(element);
        // the loop ends when the array is done
        myBuilder.addPendingEdge(element, head);
        boolean byReference = false;
        int bodyIndex = parts.size() - 1;
        for (int i = 1; i < bodyIndex; i++) {
            PsiElement part = parts.get(i);
            if (part.getNode().getElementType() == PhpElementTypes.IS_REFERENCE) {
                byReference = part.getTextLength() > 0;
            }
            else {
                visitWriteTarget(part, byReference ? Access.WRITE_REF : Access.WRITE);
            }
        }
        visitLoopBody(element, bodyIndex >= 1 ? parts.subList(bodyIndex, parts.size()) : List.of(), head);
    }

    @RequiredReadAction
    private void visitLoopBody(PsiElement loop, List<PsiElement> body, Instruction head) {
        myBreakTargets.push(loop);
        for (PsiElement part : body) {
            visit(part);
        }
        myBreakTargets.pop();
        myBuilder.addEdge(myBuilder.prevInstruction, head);
        myBuilder.flowAbrupted();
    }

    /**
     * break; break 2;
     */
    @RequiredReadAction
    private void visitBreak(PsiElement element) {
        myBuilder.startNode(element);
        PsiElement target = getBreakTarget(element);
        myBuilder.addPendingEdge(target, myBuilder.prevInstruction);
        myBuilder.flowAbrupted();
    }

    /**
     * continue; continue 2; - back to the head of the loop
     */
    @RequiredReadAction
    private void visitContinue(PsiElement element) {
        myBuilder.startNode(element);
        PsiElement target = getBreakTarget(element);
        Instruction head = target == null ? null : myBuilder.findInstructionByElement(target);
        if (head != null && LOOPS.contains(target.getNode().getElementType())) {
            myBuilder.addEdge(myBuilder.prevInstruction, head);
        }
        else {
            // continue in a switch acts like break
            myBuilder.addPendingEdge(target, myBuilder.prevInstruction);
        }
        myBuilder.flowAbrupted();
    }

    @RequiredReadAction
    private @Nullable PsiElement getBreakTarget(PsiElement statement) {
        int level = 1;
        PsiElement argument = firstComposite(statement);
        if (argument != null) {
            try {
                level = Math.max(1, Integer.parseInt(argument.getText().trim()));
            }
            catch (NumberFormatException ignored) {
                // computed levels were removed in PHP 5.4
            }
        }
        int index = 0;
        for (PsiElement target : myBreakTargets) {
            if (++index == level) {
                return target;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // tree helpers

    private static boolean isLeaf(PsiElement element) {
        return element instanceof PsiWhiteSpace || element instanceof PsiComment || element.getNode() instanceof LeafElement;
    }

    private static List<PsiElement> children(PsiElement element) {
        List<PsiElement> result = new ArrayList<>();
        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (!isLeaf(child) && child.getNode().getElementType() != PhpDocElementTypes.DOC_COMMENT) {
                result.add(child);
            }
        }
        return result;
    }

    private static @Nullable PsiElement firstComposite(PsiElement element) {
        List<PsiElement> children = children(element);
        return children.isEmpty() ? null : children.get(0);
    }

    private static @Nullable PsiElement lastComposite(PsiElement element) {
        List<PsiElement> children = children(element);
        return children.isEmpty() ? null : children.get(children.size() - 1);
    }

    private static @Nullable PsiElement nextComposite(PsiElement element) {
        for (PsiElement next = element.getNextSibling(); next != null; next = next.getNextSibling()) {
            if (!isLeaf(next)) {
                return next;
            }
        }
        return null;
    }

    private static @Nullable PsiElement findChildByType(PsiElement element, IElementType type) {
        ASTNode node = element.getNode().findChildByType(type);
        return node == null ? null : node.getPsi();
    }

    private static @Nullable PsiElement lastChildOfType(PsiElement element, IElementType type) {
        PsiElement result = null;
        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode().getElementType() == type) {
                result = child;
            }
        }
        return result;
    }

    private static boolean isArrowFunction(PsiElement element) {
        return element.getNode().findChildByType(PhpTokenTypes.FN_KEYWORD) != null;
    }

    private static boolean isYieldFrom(PsiElement element) {
        for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (isLeaf(child) && "from".equalsIgnoreCase(child.getText())) {
                return true;
            }
        }
        return false;
    }

    /**
     * &$value inside a destructuring list
     */
    private static boolean hasReferenceMark(PsiElement element) {
        return element.getNode().findChildByType(PhpTokenTypes.opBIT_AND) != null || isAfterReferenceMark(element);
    }

    private static boolean isAfterReferenceMark(PsiElement element) {
        for (PsiElement previous = element.getPrevSibling(); previous != null; previous = previous.getPrevSibling()) {
            if (previous instanceof PsiWhiteSpace || previous instanceof PsiComment) {
                continue;
            }
            return previous.getNode().getElementType() == PhpTokenTypes.opBIT_AND;
        }
        return false;
    }
}
