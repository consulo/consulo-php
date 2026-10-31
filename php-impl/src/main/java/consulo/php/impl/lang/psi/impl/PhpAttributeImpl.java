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

package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpAttribute;
import com.jetbrains.php.lang.psi.elements.PhpAttributesOwner;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpAnyExpressionArgument;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionArgument;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionClassConstantArgument;
import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionScalarArgument;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.psi.PhpNameResolver;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.php.lang.lexer.PhpTokenTypes;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpAttributeImpl extends PhpElementImpl implements PhpAttribute {
    private static final TokenSet SCALARS = TokenSet.create(PhpElementTypes.NUMBER, PhpElementTypes.CONSTANT);

    public PhpAttributeImpl(ASTNode node) {
        super(node);
    }

    @Override
    public void accept(PhpElementVisitor visitor) {
        visitor.visitPhpElement(this);
    }

    @Override
    public @Nullable ClassReference getClassReference() {
        return findChildByClass(ClassReference.class);
    }

    @RequiredReadAction
    @Override
    public @Nullable String getFQN() {
        return PhpNameResolver.resolveClassFqn(getClassReference());
    }

    @Override
    public @Nullable String getName() {
        ClassReference reference = getClassReference();
        return reference == null ? null : reference.getReferenceName();
    }

    @Override
    public @Nullable PhpAttributesOwner getOwner() {
        return PsiTreeUtil.getParentOfType(this, PhpAttributesOwner.class);
    }

    /**
     * Argument nodes: expressions (a named one is preceded by its name and a colon), {@code VARIADIC_ARGUMENT}
     */
    private List<PsiElement> getArgumentNodes() {
        List<PsiElement> result = new ArrayList<>();
        PsiElement list = findChildByType(PhpElementTypes.EXPRESSION_PARAMETER_LIST);
        if (list == null) {
            return result;
        }
        for (PsiElement child = list.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiWhiteSpace || child instanceof PsiComment || child.getNode().getElementType() == PhpTokenTypes.opCOMMA) {
                continue;
            }
            // the name and the colon of a named argument
            if (child.getNode().getElementType() == PhpTokenTypes.opCOLON
                || PhpPsiUtil.isOfType(PhpPsiUtil.getNextSiblingIgnoreWhitespace(child, true), PhpTokenTypes.opCOLON)) {
                continue;
            }
            result.add(child);
        }
        return result;
    }

    @Override
    public PsiElement[] getParameters() {
        List<PsiElement> values = new ArrayList<>();
        for (PsiElement node : getArgumentNodes()) {
            PsiElement value = valueOf(node);
            if (value != null) {
                values.add(value);
            }
        }
        return values.toArray(PsiElement.EMPTY_ARRAY);
    }

    @RequiredReadAction
    @Override
    public List<PhpAttributeArgument> getArguments() {
        List<PsiElement> nodes = getArgumentNodes();
        List<PhpAttributeArgument> result = new ArrayList<>(nodes.size());
        Parameter[] parameters = null;
        for (int i = 0; i < nodes.size(); i++) {
            PsiElement node = nodes.get(i);
            PsiElement value = valueOf(node);
            if (value == null) {
                continue;
            }

            String argumentName = PhpPsiUtil.getArgumentName(node);
            boolean named = argumentName != null;
            String name;
            if (named) {
                name = argumentName;
            }
            else {
                if (parameters == null) {
                    parameters = getConstructorParameters();
                }
                name = i < parameters.length ? stripDollar(parameters[i].getName()) : "";
            }
            result.add(new PhpAttributeArgument(name, i, toExpectedArgument(i, value), named));
        }
        return result;
    }

    private static @Nullable PsiElement valueOf(PsiElement node) {
        IElementType type = node.getNode().getElementType();
        if (type == PhpElementTypes.VARIADIC_ARGUMENT) {
            PsiElement last = node.getLastChild();
            while (last instanceof PsiWhiteSpace || last instanceof PsiComment) {
                last = last.getPrevSibling();
            }
            return last;
        }
        if (type == PhpElementTypes.CALLABLE_PLACEHOLDER) {
            return null;
        }
        return node;
    }

    private static String stripDollar(@Nullable String name) {
        if (name == null) {
            return "";
        }
        return name.startsWith("$") ? name.substring(1) : name;
    }

    /**
     * Positional arguments are named after the attribute constructor parameters
     */
    @RequiredReadAction
    private Parameter[] getConstructorParameters() {
        String fqn = getFQN();
        if (fqn == null) {
            return Parameter.EMPTY_ARRAY;
        }
        // the attribute class is often declared next to its usage - no need for the index then
        Collection<PhpClass> classes = new ArrayList<>();
        for (PhpClass phpClass : PsiTreeUtil.findChildrenOfType(getContainingFile(), PhpClass.class)) {
            if (fqn.equalsIgnoreCase(phpClass.getFQN())) {
                classes.add(phpClass);
            }
        }
        if (classes.isEmpty()) {
            classes = PhpIndex.getInstance(getProject()).getClassesByFQN(fqn);
        }
        for (PhpClass phpClass : classes) {
            Method constructor = phpClass.getConstructor();
            if (constructor != null) {
                return constructor.getParameters();
            }
        }
        return Parameter.EMPTY_ARRAY;
    }

    @RequiredReadAction
    private static PhpExpectedFunctionArgument toExpectedArgument(int index, PsiElement value) {
        String text = value.getText();
        IElementType type = value.getNode().getElementType();
        if (SCALARS.contains(type) || type == PhpElementTypes.STRING && isPlainString(value)) {
            return new PhpExpectedFunctionScalarArgument(index, text);
        }
        if (type == PhpElementTypes.CLASS_CONSTANT_REFERENCE) {
            ClassReference classReference = PsiTreeUtil.getChildOfType(value, ClassReference.class);
            String classFqn = PhpNameResolver.resolveClassFqn(classReference);
            PsiElement name = value.getLastChild();
            if (classFqn != null && name != null) {
                return new PhpExpectedFunctionClassConstantArgument(index, text, classFqn, name.getText());
            }
        }
        return new PhpAnyExpressionArgument(index, text);
    }

    /**
     * {@code 'a'} or {@code "a"} without interpolation
     */
    private static boolean isPlainString(PsiElement string) {
        PsiElement first = string.getFirstChild();
        return first != null && first == string.getLastChild()
            && (first.getNode().getElementType() == PhpTokenTypes.STRING_LITERAL || first.getNode().getElementType() == PhpTokenTypes.STRING_LITERAL_SINGLE_QUOTE);
    }
}
