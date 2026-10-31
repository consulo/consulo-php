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

package consulo.php.impl.lang.psi.resolve.types;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.util.RecursionManager;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.psi.PhpNameResolver;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.lang.lexer.PhpTokenTypes;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Local types: computed from the PSI of one file only (no index, no resolve), safe in {@link PhpTypeProvider4#getType}.
 * Whatever needs other files is left as a signature for {@link PhpTypeResolver}:
 * {@code #C\Foo} class, {@code #M#C\Foo.bar} method, {@code #P#C\Foo.prop} field, {@code #K#C\Foo.BAR} class constant,
 * {@code #F\foo} function, {@code #D\FOO} constant, or {@code #<key>...} of a type provider.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpTypeInference {
    private static final Set<String> BUILTIN_TYPES = Set.of("int", "integer", "float", "double", "string", "bool", "boolean", "array", "callable",
        "iterable", "object", "mixed", "void", "null", "never", "false", "true", "resource", "static", "self", "parent", "$this", "number",
        "scalar", "list", "non-empty-string", "non-empty-array", "class-string", "positive-int", "negative-int", "numeric", "array-key");

    private static final TokenSet BOOLEAN_EXPRESSIONS = TokenSet.create(PhpElementTypes.LOGICAL_EXPRESSION, PhpElementTypes.LITERAL_LOGICAL_EXPRESSION,
        PhpElementTypes.EQUALITY_EXPRESSION, PhpElementTypes.RELATIONAL_EXPRESSION, PhpElementTypes.INSTANCEOF_EXPRESSION,
        PhpElementTypes.ISSET_EXPRESSION, PhpElementTypes.EMPTY_EXPRESSION);

    private static final TokenSet STRING_EXPRESSIONS = TokenSet.create(PhpElementTypes.STRING, PhpElementTypes.HEREDOC, PhpElementTypes.SHELL_COMMAND,
        PhpElementTypes.CONCATENATION_EXPRESSION);

    private static final TokenSet FUNCTION_SCOPES = TokenSet.create(PhpElementTypes.FUNCTION, PhpElementTypes.ANONYMOUS_FUNCTION,
        PhpStubElements.CLASS_METHOD);

    private PhpTypeInference() {
    }

    /**
     * Local type of an expression plus the types contributed by {@link PhpTypeProvider4}s
     */
    @RequiredReadAction
    public static PhpType getType(PsiElement element) {
        PhpType result = RecursionManager.doPreventingRecursion(element, false, () -> {
            PhpType.PhpTypeBuilder builder = PhpType.builder();
            builder.add(getLocalType(element));
            for (PhpTypeProvider4 provider : PhpTypeProvider4.EP_NAME.getExtensionList()) {
                PhpType provided = provider.getType(element);
                if (provided != null) {
                    builder.add(provided);
                }
            }
            return builder.build();
        });
        return result == null ? PhpType.EMPTY : result;
    }

    /**
     * Signature of a method, field, constant or function reference, without type provider contributions
     */
    @RequiredReadAction
    public static String getSignature(PsiElement reference) {
        return String.join("|", getLocalType(reference).getTypes());
    }

    @RequiredReadAction
    private static PhpType getLocalType(PsiElement element) {
        IElementType type = element.getNode().getElementType();
        if (type == PhpElementTypes.VARIABLE_REFERENCE) {
            return getVariableType(element);
        }
        if (type == PhpElementTypes.NEW_EXPRESSION) {
            return getNewType(element);
        }
        if (type == PhpElementTypes.METHOD_REFERENCE) {
            return getMemberType(element, 'M');
        }
        if (type == PhpElementTypes.FIELD_REFERENCE) {
            return getMemberType(element, 'P');
        }
        if (type == PhpElementTypes.CLASS_CONSTANT_REFERENCE) {
            return isClassNameFetch(element) ? PhpType.STRING : getMemberType(element, 'K');
        }
        if (type == PhpElementTypes.FUNCTION_REFERENCE) {
            return getFunctionCallType(element);
        }
        if (type == PhpElementTypes.CONSTANT) {
            return getConstantType(element);
        }
        if (STRING_EXPRESSIONS.contains(type)) {
            return PhpType.STRING;
        }
        if (type == PhpElementTypes.NUMBER) {
            return getNumberType(element.getText());
        }
        if (type == PhpElementTypes.ARRAY_CREATION_EXPRESSION) {
            return PhpType.ARRAY;
        }
        if (BOOLEAN_EXPRESSIONS.contains(type)) {
            return PhpType.BOOLEAN;
        }
        if (type == PhpElementTypes.ADDITIVE_EXPRESSION || type == PhpElementTypes.MULTIPLICATIVE_EXPRESSION || type == PhpElementTypes.POW_EXPRESSION) {
            return PhpType.FLOAT_INT;
        }
        if (type == PhpElementTypes.SHIFT_EXPRESSION || type == PhpElementTypes.BIT_EXPRESSION || type == PhpElementTypes.PRINT_EXPRESSION) {
            return PhpType.INT;
        }
        if (type == PhpElementTypes.UNARY_EXPRESSION) {
            ASTNode operator = element.getNode().getFirstChildNode();
            if (operator != null && operator.getElementType() == PhpTokenTypes.opNOT) {
                return PhpType.BOOLEAN;
            }
            return typeOfChildren(element, 0, 1);
        }
        if (type == PhpElementTypes.TYPE_CAST_EXPRESSION) {
            return getCastType(element);
        }
        if (type == PhpElementTypes.TERNARY_EXPRESSION) {
            return typeOfChildren(element, 1, 2);
        }
        if (type == PhpElementTypes.ELVIS_EXPRESSION) {
            return typeOfChildren(element, 0, 2);
        }
        if (type == PhpElementTypes.COALESCE_EXPRESSION) {
            List<PsiElement> operands = operands(element);
            PhpType.PhpTypeBuilder builder = PhpType.builder();
            if (!operands.isEmpty()) {
                builder.add(getType(operands.get(0)).filterNull());
            }
            if (operands.size() > 1) {
                builder.add(getType(operands.get(operands.size() - 1)));
            }
            return builder.build();
        }
        if (type == PhpElementTypes.PARENTHESIZED_EXPRESSION || type == PhpElementTypes.CLONE_EXPRESSION || type == PhpElementTypes.SILENCE_EXPRESSION) {
            return typeOfChildren(element, 0, 1);
        }
        if (type == PhpElementTypes.ASSIGNMENT_EXPRESSION || type == PhpElementTypes.SELF_ASSIGNMENT_EXPRESSION) {
            List<PsiElement> operands = operands(element);
            return operands.size() < 2 ? PhpType.EMPTY : getType(operands.get(operands.size() - 1));
        }
        if (type == PhpElementTypes.ANONYMOUS_FUNCTION || type == PhpElementTypes.CLOSURE) {
            return PhpType.CLOSURE;
        }
        if (type == PhpElementTypes.ARRAY_ACCESS_EXPRESSION) {
            List<PsiElement> operands = operands(element);
            return operands.isEmpty() ? PhpType.EMPTY : elementTypeOf(getType(operands.get(0)));
        }
        if (type == PhpElementTypes.MATCH_EXPRESSION) {
            PhpType.PhpTypeBuilder builder = PhpType.builder();
            for (PsiElement arm : element.getChildren()) {
                if (arm.getNode().getElementType() == PhpElementTypes.MATCH_ARM) {
                    List<PsiElement> armOperands = operands(arm);
                    if (!armOperands.isEmpty()) {
                        builder.add(getType(armOperands.get(armOperands.size() - 1)));
                    }
                }
            }
            return builder.build();
        }
        if (element instanceof PhpClass phpClass) {
            String fqn = phpClass.getFQN();
            return fqn == null ? PhpType.EMPTY : PhpType.builder().add(fqn).build();
        }
        return PhpType.EMPTY;
    }

    /**
     * Child expressions, whitespace, comments and tokens skipped
     */
    private static List<PsiElement> operands(PsiElement element) {
        List<PsiElement> result = new ArrayList<>();
        for (PsiElement child : element.getChildren()) {
            if (!(child instanceof PsiWhiteSpace) && !(child instanceof PsiComment)) {
                result.add(child);
            }
        }
        return result;
    }

    private static PhpType typeOfChildren(PsiElement element, int from, int to) {
        List<PsiElement> operands = operands(element);
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        for (int i = from; i <= to && i < operands.size(); i++) {
            builder.add(getType(operands.get(i)));
        }
        return builder.build();
    }

    private static PhpType getNumberType(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        boolean hexOrBinary = lower.startsWith("0x") || lower.startsWith("0b");
        if (!hexOrBinary && (lower.contains(".") || lower.contains("e"))) {
            return PhpType.FLOAT;
        }
        return PhpType.INT;
    }

    private static PhpType getCastType(PsiElement element) {
        ASTNode cast = element.getNode().getFirstChildNode();
        if (cast == null) {
            return PhpType.EMPTY;
        }
        IElementType type = cast.getElementType();
        if (type == PhpTokenTypes.opINTEGER_CAST) {
            return PhpType.INT;
        }
        if (type == PhpTokenTypes.opFLOAT_CAST) {
            return PhpType.FLOAT;
        }
        if (type == PhpTokenTypes.opSTRING_CAST) {
            return PhpType.STRING;
        }
        if (type == PhpTokenTypes.opBOOLEAN_CAST) {
            return PhpType.BOOLEAN;
        }
        if (type == PhpTokenTypes.opARRAY_CAST) {
            return PhpType.ARRAY;
        }
        if (type == PhpTokenTypes.opOBJECT_CAST) {
            return PhpType.builder().add("\\stdClass").build();
        }
        if (type == PhpTokenTypes.opUNSET_CAST) {
            return PhpType.NULL;
        }
        return PhpType.EMPTY;
    }

    private static PhpType getConstantType(PsiElement element) {
        String name = element.getText();
        String lower = name.toLowerCase(Locale.ROOT);
        if (lower.equals("true") || lower.equals("false")) {
            return PhpType.BOOLEAN;
        }
        if (lower.equals("null")) {
            return PhpType.NULL;
        }
        if (lower.startsWith("__") && lower.endsWith("__")) {
            // magic constants: __CLASS__, __LINE__, ...
            return lower.equals("__line__") ? PhpType.INT : PhpType.STRING;
        }
        String fqn = name.startsWith("\\") ? name : "\\" + name;
        return PhpType.builder().add("#D" + fqn).build();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // references

    @RequiredReadAction
    private static PhpType getNewType(PsiElement element) {
        ClassReference reference = PsiTreeUtil.getChildOfType(element, ClassReference.class);
        if (reference != null) {
            String fqn = PhpNameResolver.resolveClassFqn(reference);
            return fqn == null ? PhpType.EMPTY : PhpType.builder().add(fqn).build();
        }
        PsiElement anonymousClass = findChildOfType(element, PhpElementTypes.ANONYMOUS_CLASS);
        if (anonymousClass instanceof PhpClass phpClass) {
            String fqn = phpClass.getFQN();
            return fqn == null ? PhpType.EMPTY : PhpType.builder().add(fqn).build();
        }
        return PhpType.EMPTY;
    }

    private static boolean isClassNameFetch(PsiElement element) {
        ASTNode last = element.getNode().getLastChildNode();
        return last != null && last.getElementType() == PhpTokenTypes.kwCLASS;
    }

    /**
     * {@code $a->name()}, {@code Foo::name()}, {@code $a->prop}, {@code Foo::$prop}, {@code Foo::CONST}
     */
    @RequiredReadAction
    private static PhpType getMemberType(PsiElement element, char key) {
        ASTNode node = element.getNode();
        ASTNode qualifierNode = node.getFirstChildNode();
        if (qualifierNode == null) {
            return PhpType.EMPTY;
        }
        String name = getMemberName(node);
        if (name == null) {
            return PhpType.EMPTY;
        }

        PhpType qualifierType;
        PsiElement qualifier = qualifierNode.getPsi();
        if (qualifier instanceof ClassReference classReference) {
            String fqn = PhpNameResolver.resolveClassFqn(classReference);
            qualifierType = fqn == null ? PhpType.EMPTY : PhpType.builder().add(fqn).build();
        }
        else {
            qualifierType = getType(qualifier);
        }

        PhpType.PhpTypeBuilder builder = PhpType.builder();
        for (String type : qualifierType.getTypes()) {
            if (!type.startsWith("#") && PhpType.isPrimitiveType(type)) {
                continue;
            }
            builder.add("#" + key + (type.startsWith("#") ? type : "#C" + type) + "." + name);
        }
        return builder.build();
    }

    /**
     * Name after {@code ->}, {@code ?->} or {@code ::}; {@code $} stripped from static properties
     */
    private static @Nullable String getMemberName(ASTNode node) {
        boolean afterOperator = false;
        for (ASTNode child = node.getFirstChildNode(); child != null; child = child.getTreeNext()) {
            IElementType type = child.getElementType();
            if (type == PhpTokenTypes.ARROW || type == PhpTokenTypes.NULLSAFE_ARROW || type == PhpTokenTypes.SCOPE_RESOLUTION) {
                afterOperator = true;
                continue;
            }
            if (!afterOperator) {
                continue;
            }
            if (type == PhpTokenTypes.IDENTIFIER) {
                return child.getText();
            }
            if (type == PhpTokenTypes.VARIABLE) {
                return child.getText().substring(1);
            }
            if (type != PhpTokenTypes.WHITE_SPACE) {
                return null;
            }
        }
        return null;
    }

    @RequiredReadAction
    private static PhpType getFunctionCallType(PsiElement element) {
        ASTNode node = element.getNode();
        ASTNode name = node.findChildByType(PhpTokenTypes.IDENTIFIER);
        if (name == null) {
            return PhpType.EMPTY;
        }
        ASTNode namespace = node.findChildByType(PhpElementTypes.NAMESPACE_REFERENCE);
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        if (namespace != null) {
            String qualified = PhpNameResolver.nameText(namespace.getPsi()) + name.getText();
            builder.add("#F" + PhpNameResolver.resolveName(element, qualified));
            return builder.build();
        }
        // unqualified function names fall back to the global namespace
        String currentNamespace = PhpNameResolver.getNamespaceName(element);
        if (!currentNamespace.isEmpty()) {
            builder.add("#F\\" + currentNamespace + "\\" + name.getText());
        }
        builder.add("#F\\" + name.getText());
        return builder.build();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // variables

    /**
     * Union of everything assigned to the variable in its function, its parameter, {@code foreach}, {@code catch} and inline {@code @var}
     */
    @RequiredReadAction
    private static PhpType getVariableType(PsiElement variable) {
        String name = variable.getText();
        if (!name.startsWith("$") || name.length() < 2) {
            return PhpType.EMPTY;
        }
        if (name.equals("$this")) {
            PhpClass phpClass = PsiTreeUtil.getParentOfType(variable, PhpClass.class);
            String fqn = phpClass == null ? null : phpClass.getFQN();
            return fqn == null ? PhpType.EMPTY : PhpType.builder().add(fqn).build();
        }

        PsiElement scope = getScope(variable);
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        if (scope instanceof Function function) {
            for (Parameter parameter : function.getParameters()) {
                if (name.equals(parameter.getName()) || name.equals("$" + parameter.getName())) {
                    builder.add(parameter.getType());
                }
            }
        }
        collectAssignedTypes(scope, variable, name, builder);
        return builder.build();
    }

    private static PsiElement getScope(PsiElement element) {
        PsiElement parent = element.getParent();
        while (parent != null && !(parent instanceof PsiFile)) {
            if (FUNCTION_SCOPES.contains(parent.getNode().getElementType()) && !isArrowFunction(parent)) {
                return parent;
            }
            parent = parent.getParent();
        }
        return parent == null ? element.getContainingFile() : parent;
    }

    private static boolean isArrowFunction(PsiElement element) {
        return element.getNode().findChildByType(PhpTokenTypes.FN_KEYWORD) != null;
    }

    @RequiredReadAction
    private static void collectAssignedTypes(PsiElement root, PsiElement variable, String name, PhpType.PhpTypeBuilder builder) {
        for (PsiElement child : root.getChildren()) {
            IElementType type = child.getNode().getElementType();
            if (child != root && FUNCTION_SCOPES.contains(type) && !isArrowFunction(child) || child instanceof PhpClass) {
                // nested functions and classes have their own variables
                continue;
            }
            if (type == PhpElementTypes.ASSIGNMENT_EXPRESSION) {
                List<PsiElement> operands = operands(child);
                if (operands.size() >= 2 && isVariable(operands.get(0), name)) {
                    builder.add(getType(operands.get(operands.size() - 1)));
                }
            }
            else if (type == PhpElementTypes.FOREACH) {
                collectForeachType(child, variable, name, builder);
            }
            else if (type == PhpElementTypes.CATCH) {
                ASTNode catchVariable = child.getNode().findChildByType(PhpElementTypes.VARIABLE_REFERENCE);
                if (catchVariable != null && name.equals(catchVariable.getText())) {
                    for (ClassReference reference : PsiTreeUtil.getChildrenOfTypeAsList(child, ClassReference.class)) {
                        String fqn = PhpNameResolver.resolveClassFqn(reference);
                        if (fqn != null) {
                            builder.add(fqn);
                        }
                    }
                }
            }
            else if (child instanceof PhpDocComment docComment) {
                collectInlineVarType(docComment, name, builder);
            }
            collectAssignedTypes(child, variable, name, builder);
        }
    }

    private static boolean isVariable(PsiElement element, String name) {
        return element.getNode().getElementType() == PhpElementTypes.VARIABLE_REFERENCE && name.equals(element.getText());
    }

    /**
     * {@code foreach ($items as $key => $value)} - the value is an element of {@code $items}
     */
    @RequiredReadAction
    private static void collectForeachType(PsiElement foreach, PsiElement variable, String name, PhpType.PhpTypeBuilder builder) {
        List<PsiElement> operands = new ArrayList<>();
        for (PsiElement operand : operands(foreach)) {
            if (operand.getNode().getElementType() != PhpElementTypes.IS_REFERENCE) {
                operands.add(operand);
            }
        }
        if (operands.size() < 2) {
            return;
        }
        // operands: iterated expression, [key], value, body...
        PsiElement value = operands.size() >= 3 && isVariable(operands.get(2), name) ? operands.get(2) : operands.get(1);
        if (value != variable && isVariable(value, name)) {
            builder.add(elementTypeOf(getType(operands.get(0))));
        }
    }

    /**
     * Element type of an array type; unresolved types become {@code #E} signatures
     */
    private static PhpType elementTypeOf(PhpType arrayType) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        for (String type : arrayType.getTypes()) {
            if (type.startsWith("#")) {
                builder.add("#E" + type);
            }
            else if (type.endsWith("[]")) {
                builder.add(type.substring(0, type.length() - 2));
            }
        }
        return builder.build();
    }

    /**
     * {@code /** @var Foo $name *\/}
     */
    @RequiredReadAction
    private static void collectInlineVarType(PhpDocComment docComment, String name, PhpType.PhpTypeBuilder builder) {
        for (PhpDocTag tag : docComment.getTagElementsByName("@var")) {
            String tagValue = tag.getTagValue();
            if (!tagValue.contains(name)) {
                continue;
            }
            for (PhpDocType docType : PsiTreeUtil.getChildrenOfTypeAsList(tag, PhpDocType.class)) {
                builder.add(resolveDocType(docType, docType.getName()));
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // declarations

    /**
     * Declared return type and {@code @return}; {@code static}, {@code self} and {@code $this} are completed by {@link PhpTypeResolver}
     */
    @RequiredReadAction
    public static PhpType getReturnType(Function function) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        PsiElement returnType = findChildOfType(function, PhpElementTypes.RETURN_TYPE);
        if (returnType != null) {
            builder.add(getDeclaredType(returnType));
        }
        PhpDocComment docComment = function.getDocComment();
        if (docComment != null) {
            for (PhpDocTag tag : docComment.getTagElementsByName("@return")) {
                addDocTypes(tag, builder);
            }
        }
        return builder.build();
    }

    /**
     * Declared type, {@code @var} and the default value
     */
    @RequiredReadAction
    public static PhpType getFieldType(PhpNamedElement field) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        PsiElement list = field.getParent();
        builder.add(getDeclaredType(list != null && !(list instanceof PhpClass) ? list : field));
        PhpDocComment docComment = field.getDocComment();
        if (docComment != null) {
            for (PhpDocTag tag : docComment.getTagElementsByName("@var")) {
                addDocTypes(tag, builder);
            }
        }
        ASTNode assign = field.getNode().findChildByType(PhpTokenTypes.opASGN);
        if (assign != null) {
            for (ASTNode value = assign.getTreeNext(); value != null; value = value.getTreeNext()) {
                if (value.getPsi() instanceof PsiWhiteSpace || value.getPsi() instanceof PsiComment) {
                    continue;
                }
                if (value.getElementType() != PhpElementTypes.CONSTANT || !"null".equalsIgnoreCase(value.getText())) {
                    builder.add(getType(value.getPsi()));
                }
                break;
            }
        }
        return builder.build();
    }

    /**
     * Declared type and {@code @param} of the function
     */
    @RequiredReadAction
    public static PhpType getParameterType(Parameter parameter) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        builder.add(getDeclaredType(parameter));
        Function function = PsiTreeUtil.getParentOfType(parameter, Function.class);
        PhpDocComment docComment = function == null ? null : function.getDocComment();
        if (docComment != null) {
            String name = parameter.getName();
            PhpDocParamTag tag = docComment.getParamTagByName(name.startsWith("$") ? name.substring(1) : name);
            if (tag != null) {
                addDocTypes(tag, builder);
            }
        }
        if (parameter.isVariadic()) {
            return builder.build().pluralise();
        }
        return builder.build();
    }

    /**
     * Type written in code: class references and {@code ?} directly under the owner
     */
    @RequiredReadAction
    public static PhpType getDeclaredType(PsiElement owner) {
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        for (ASTNode child = owner.getNode().getFirstChildNode(); child != null; child = child.getTreeNext()) {
            if (child.getElementType() == PhpElementTypes.TYPE_DECLARATION || child.getElementType() == PhpElementTypes.FIELD_TYPE) {
                // the type of a parameter, property, constant or enum is wrapped into its declaration
                builder.add(getDeclaredType(child.getPsi()));
            }
            else if (child.getElementType() == PhpTokenTypes.opQUEST) {
                builder.add(PhpType.NULL);
            }
            else if (child.getPsi() instanceof ClassReference reference) {
                builder.add(resolveTypeName(reference, PhpNameResolver.nameText(reference)));
            }
            else if (child.getElementType() == PhpTokenTypes.VARIABLE || child.getElementType() == PhpTokenTypes.opASGN
                || child.getElementType() == PhpStubElements.CLASS_FIELD) {
                break;
            }
        }
        return builder.build();
    }

    private static void addDocTypes(PhpDocTag tag, PhpType.PhpTypeBuilder builder) {
        for (PhpDocType docType : PsiTreeUtil.getChildrenOfTypeAsList(tag, PhpDocType.class)) {
            builder.add(resolveDocType(docType, docType.getName()));
        }
    }

    /**
     * Type of a PHPDoc type element, names resolved against the namespace and imports
     */
    @RequiredReadAction
    public static PhpType getDocType(PhpDocType docType) {
        return resolveDocType(docType, docType.getName());
    }

    /**
     * {@code ?Foo}, {@code Foo[]}, {@code array<int, Foo>}, {@code \\Foo\\Bar}, {@code $this}
     */
    private static PhpType resolveDocType(PsiElement context, String text) {
        String type = text.trim();
        PhpType.PhpTypeBuilder builder = PhpType.builder();
        if (type.startsWith("?")) {
            builder.add(PhpType.NULL);
            type = type.substring(1);
        }
        int generic = type.indexOf('<');
        if (generic > 0) {
            String base = type.substring(0, generic).toLowerCase(Locale.ROOT);
            if (base.equals("array") || base.equals("list") || base.equals("iterable") || base.equals("non-empty-array")) {
                return builder.add(PhpType.ARRAY).build();
            }
            type = type.substring(0, generic);
        }
        int arrays = 0;
        while (type.endsWith("[]")) {
            type = type.substring(0, type.length() - 2);
            arrays++;
        }
        if (type.isEmpty() || type.startsWith("(") || type.startsWith("{")) {
            return builder.build();
        }
        PhpType resolved = resolveTypeName(context, type);
        for (int i = 0; i < arrays; i++) {
            resolved = resolved.pluralise();
        }
        return builder.add(resolved).build();
    }

    @RequiredReadAction
    private static PhpType resolveTypeName(PsiElement context, String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        if (lower.equals("self")) {
            PhpClass phpClass = PsiTreeUtil.getParentOfType(context, PhpClass.class);
            String fqn = phpClass == null ? null : phpClass.getFQN();
            return PhpType.builder().add(fqn == null ? "static" : fqn).build();
        }
        if (lower.equals("$this")) {
            return PhpType.STATIC;
        }
        if (BUILTIN_TYPES.contains(lower)) {
            return PhpType.builder().add(lower.equals("non-empty-string") || lower.equals("class-string") ? "string"
                : lower.equals("list") || lower.equals("non-empty-array") ? "array"
                : lower.endsWith("-int") ? "int" : lower).build();
        }
        if (name.startsWith("\\")) {
            return PhpType.builder().add(name).build();
        }
        return PhpType.builder().add(PhpNameResolver.resolveName(context, name)).build();
    }

    private static @Nullable PsiElement findChildOfType(PsiElement element, IElementType type) {
        ASTNode child = element.getNode().findChildByType(type);
        return child == null ? null : child.getPsi();
    }
}
