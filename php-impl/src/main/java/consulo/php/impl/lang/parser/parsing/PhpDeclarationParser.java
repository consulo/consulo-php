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

package consulo.php.impl.lang.parser.parsing;

import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.language.parser.PsiBuilder;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * Attributes, functions, closures, parameters, classes, interfaces, traits, enums and class members.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpDeclarationParser implements PhpTokenTypes {
    private static final TokenSet CLASS_KEYWORDS = TokenSet.create(kwCLASS, INTERFACE_KEYWORD, TRAIT_KEYWORD);

    private static final TokenSet CLASS_MODIFIERS = TokenSet.create(ABSTRACT_KEYWORD, FINAL_KEYWORD, READONLY_KEYWORD);

    private PhpDeclarationParser() {
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // attributes

    /**
     * {@code #[A, B(1)] #[C]}
     */
    public static void parseAttributes(PhpPsiBuilder builder) {
        while (builder.compare(ATTRIBUTE_START)) {
            PsiBuilder.Marker list = builder.mark();
            builder.advanceLexer();
            while (!builder.eof() && !builder.compare(RBRACKET)) {
                PsiBuilder.Marker attribute = builder.mark();
                if (PhpNameParser.parseClassReference(builder, false) == null) {
                    attribute.drop();
                    builder.error(PhpParserErrors.expected("attribute name"));
                    break;
                }
                if (builder.compare(LPAREN)) {
                    PhpExpressionParser.parseArguments(builder);
                }
                attribute.done(PhpElementTypes.ATTRIBUTE);
                if (!builder.compareAndEat(opCOMMA)) {
                    break;
                }
            }
            builder.match(RBRACKET);
            list.done(PhpElementTypes.ATTRIBUTES_LIST);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // functions

    /**
     * {@code function name(} or {@code function &name(} - a declaration, not a closure
     */
    public static boolean isFunctionDeclarationStart(PhpPsiBuilder builder) {
        if (!builder.compare(kwFUNCTION)) {
            return false;
        }
        IElementType next = builder.lookAhead(1);
        if (next == opBIT_AND) {
            next = builder.lookAhead(2);
        }
        return next != null && next != LPAREN && PhpParserUtil.isIdentifierLike(next);
    }

    /**
     * {@code #[A] function &name(params): type { body }}
     */
    public static void parseFunction(PhpPsiBuilder builder) {
        PsiBuilder.Marker function = builder.mark();
        parseAttributes(builder);
        builder.match(kwFUNCTION);
        parseIsReference(builder);
        if (!builder.compareAndEat(IDENTIFIER)) {
            builder.error(PhpParserErrors.expected("function name"));
            PhpParserUtil.eatIdentifierLike(builder);
        }
        parseParameterList(builder);
        PhpNameParser.parseReturnTypeIfColon(builder);
        parseBody(builder);
        function.done(PhpElementTypes.FUNCTION);
    }

    private static void parseIsReference(PhpPsiBuilder builder) {
        PsiBuilder.Marker isReference = builder.mark();
        builder.compareAndEat(opBIT_AND);
        isReference.done(PhpElementTypes.IS_REFERENCE);
    }

    private static void parseBody(PhpPsiBuilder builder) {
        if (builder.compareAndEat(LBRACE)) {
            builder.enterFunction();
            try {
                PhpStatementParser.parseStatementList(builder, TokenSet.create(RBRACE));
            }
            finally {
                builder.exitFunction();
            }
            builder.match(RBRACE);
        }
        else {
            builder.error(PhpParserErrors.expected(LBRACE));
        }
    }

    /**
     * {@code #[A] static function &(params) use ($a, &$b): type { body }} and {@code static fn &(params): type => expr}
     */
    public static IElementType parseClosure(PhpPsiBuilder builder) {
        PsiBuilder.Marker closure = builder.mark();
        parseAttributes(builder);
        builder.compareAndEat(STATIC_KEYWORD);

        if (builder.compareAndEat(FN_KEYWORD)) {
            parseIsReference(builder);
            parseParameterList(builder);
            PhpNameParser.parseReturnTypeIfColon(builder);
            builder.match(HASH_ARRAY);
            PhpExpressionParser.parseRequired(builder);
        }
        else if (builder.compareAndEat(kwFUNCTION)) {
            parseIsReference(builder);
            parseParameterList(builder);
            if (builder.compare(USE_KEYWORD)) {
                parseClosureUses(builder);
            }
            PhpNameParser.parseReturnTypeIfColon(builder);
            parseBody(builder);
        }
        else {
            // attributes not followed by a closure
            builder.error(PhpParserErrors.expected("function"));
        }
        closure.done(PhpElementTypes.ANONYMOUS_FUNCTION);
        // the function is wrapped by the closure expression
        closure.precede().done(PhpElementTypes.CLOSURE);
        return PhpElementTypes.CLOSURE;
    }

    private static void parseClosureUses(PhpPsiBuilder builder) {
        PsiBuilder.Marker uses = builder.mark();
        builder.advanceLexer();
        builder.match(LPAREN);
        while (!builder.eof() && !builder.compare(RPAREN)) {
            builder.compareAndEat(opBIT_AND);
            PsiBuilder.Marker variable = builder.mark();
            if (!builder.compareAndEat(VARIABLE)) {
                variable.drop();
                builder.error(PhpParserErrors.expected("variable"));
                break;
            }
            variable.done(PhpElementTypes.VARIABLE_REFERENCE);
            if (!builder.compareAndEat(opCOMMA)) {
                break;
            }
        }
        builder.match(RPAREN);
        uses.done(PhpElementTypes.CLOSURE_USES);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // parameters

    public static void parseParameterList(PhpPsiBuilder builder) {
        PsiBuilder.Marker list = builder.mark();
        builder.match(LPAREN);
        while (!builder.eof() && !builder.compare(RPAREN)) {
            if (!parseParameter(builder)) {
                builder.error(PhpParserErrors.expected("parameter"));
                break;
            }
            if (!builder.compareAndEat(opCOMMA)) {
                break;
            }
        }
        builder.match(RPAREN);
        list.done(PhpElementTypes.PARAMETER_LIST);
    }

    /**
     * {@code #[A] public readonly private(set) ?Type &...$name = default { hooks }}
     */
    private static boolean parseParameter(PhpPsiBuilder builder) {
        PsiBuilder.Marker parameter = builder.mark();
        parseAttributes(builder);

        PsiBuilder.Marker modifiers = builder.mark();
        boolean promoted = parseMemberModifierTokens(builder);
        if (promoted) {
            modifiers.done(PhpElementTypes.MODIFIER_LIST);
        }
        else {
            modifiers.drop();
        }

        if (!builder.compare(VARIABLE) && !builder.compare(opBIT_AND) && !builder.compare(ELLIPSIS)) {
            PhpNameParser.parseTypeDeclaration(builder, PhpElementTypes.TYPE_DECLARATION);
        }
        builder.compareAndEat(opBIT_AND);
        builder.compareAndEat(ELLIPSIS);
        if (!builder.compareAndEat(VARIABLE)) {
            parameter.rollbackTo();
            return false;
        }
        if (builder.compare(opASGN)) {
            PsiBuilder.Marker defaultValue = builder.mark();
            builder.advanceLexer();
            PhpExpressionParser.parseRequired(builder);
            defaultValue.done(PhpElementTypes.PARAMETER_DEFAULT_VALUE);
        }
        if (builder.compare(LBRACE)) {
            // PHP 8.4: hooks on a promoted property
            parsePropertyHooks(builder);
        }
        parameter.done(PhpElementTypes.PARAMETER);
        return true;
    }

    /**
     * Visibility (with PHP 8.4 asymmetric {@code private(set)}), {@code static}, {@code abstract}, {@code final}, {@code readonly}, {@code var}
     *
     * @return true if any modifier was eaten
     */
    private static boolean parseMemberModifierTokens(PhpPsiBuilder builder) {
        boolean any = false;
        while (true) {
            PhpParserUtil.remapReadonly(builder, true);
            if (!builder.compare(PhpParserUtil.MEMBER_MODIFIERS)) {
                return any;
            }
            boolean visibility = builder.compare(PhpParserUtil.VISIBILITY);
            builder.advanceLexer();
            any = true;
            if (visibility && builder.compare(LPAREN) && !isWhitespaceBefore(builder) && builder.rawLookup(2) == RPAREN
                && "set".equalsIgnoreCase(lookAheadText(builder))) {
                builder.advanceLexer();
                builder.advanceLexer();
                builder.advanceLexer();
            }
        }
    }

    private static boolean isWhitespaceBefore(PhpPsiBuilder builder) {
        IElementType previous = builder.rawLookup(-1);
        return previous == WHITE_SPACE || previous == LINE_COMMENT || previous == C_STYLE_COMMENT;
    }

    private static String lookAheadText(PhpPsiBuilder builder) {
        PsiBuilder.Marker probe = builder.mark();
        builder.advanceLexer();
        String text = builder.getTokenText();
        probe.rollbackTo();
        return text == null ? "" : text;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // classes

    /**
     * {@code abstract class}, {@code final readonly class}, {@code interface}, {@code trait}, {@code enum Name}
     */
    public static boolean isClassStart(PhpPsiBuilder builder) {
        PsiBuilder.Marker probe = builder.mark();
        try {
            parseAttributes(builder);
            while (true) {
                if (PhpParserUtil.isContextual(builder, "readonly") && builder.lookAhead(1) != LPAREN) {
                    builder.advanceLexer();
                    continue;
                }
                if (builder.compare(ABSTRACT_KEYWORD) || builder.compare(FINAL_KEYWORD)) {
                    builder.advanceLexer();
                    continue;
                }
                break;
            }
            if (builder.compare(CLASS_KEYWORDS)) {
                return true;
            }
            return isEnumStart(builder);
        }
        finally {
            probe.rollbackTo();
        }
    }

    private static boolean isEnumStart(PhpPsiBuilder builder) {
        return PhpParserUtil.isContextual(builder, "enum") && builder.lookAhead(1) == IDENTIFIER;
    }

    public static void parseClass(PhpPsiBuilder builder) {
        PsiBuilder.Marker declaration = builder.mark();
        parseAttributes(builder);
        boolean modifiers = parseClassModifiers(builder);

        IElementType kind = builder.getTokenType();
        if (isEnumStart(builder)) {
            builder.remapCurrentToken(ENUM_KEYWORD);
            kind = ENUM_KEYWORD;
            builder.advanceLexer();
        }
        else if (!builder.compareAndEat(CLASS_KEYWORDS)) {
            builder.error(PhpParserErrors.expected("class"));
        }
        if (modifiers && kind != kwCLASS) {
            builder.error("Modifiers are allowed only for classes");
        }

        if (!builder.compareAndEat(IDENTIFIER)) {
            builder.error(PhpParserErrors.expected("class name"));
        }
        if (kind == ENUM_KEYWORD && builder.compareAndEat(opCOLON)) {
            // backed enum
            PhpNameParser.parseTypeDeclaration(builder, PhpElementTypes.TYPE_DECLARATION);
        }
        parseClassTail(builder, kind);
        declaration.done(PhpElementTypes.CLASS);
    }

    /**
     * @return true if any modifier was eaten
     */
    public static boolean parseClassModifiers(PhpPsiBuilder builder) {
        PsiBuilder.Marker modifiers = builder.mark();
        boolean any = false;
        while (true) {
            PhpParserUtil.remapReadonly(builder);
            if (!builder.compareAndEat(CLASS_MODIFIERS)) {
                break;
            }
            any = true;
        }
        modifiers.done(PhpElementTypes.MODIFIER_LIST);
        return any;
    }

    /**
     * {@code extends A, B implements C, D { members }}
     *
     * @param kind {@code class}, {@code interface}, {@code trait} or {@code enum} keyword
     */
    public static void parseClassTail(PhpPsiBuilder builder, IElementType kind) {
        if (kind == TRAIT_KEYWORD && (builder.compare(kwEXTENDS) || builder.compare(kwIMPLEMENTS))
            || kind == INTERFACE_KEYWORD && builder.compare(kwIMPLEMENTS)
            || kind == ENUM_KEYWORD && builder.compare(kwEXTENDS)) {
            builder.error(PhpParserErrors.expected(LBRACE));
        }
        parseTypeList(builder, kwEXTENDS, PhpElementTypes.EXTENDS_LIST);
        parseTypeList(builder, kwIMPLEMENTS, PhpElementTypes.IMPLEMENTS_LIST);
        if (!builder.compareAndEat(LBRACE)) {
            builder.error(PhpParserErrors.expected(LBRACE));
            return;
        }
        while (!builder.eof() && !builder.compare(RBRACE)) {
            int offset = builder.getCurrentOffset();
            parseClassMember(builder);
            if (offset == builder.getCurrentOffset()) {
                builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
                builder.advanceLexer();
            }
        }
        builder.match(RBRACE);
    }

    private static void parseTypeList(PhpPsiBuilder builder, IElementType keyword, IElementType elementType) {
        PsiBuilder.Marker list = builder.mark();
        if (builder.compare(keyword) && PhpParserUtil.isRawNext(builder, SLASH)) {
            // PHP 8: 'implements\A' is a single name
            builder.error(PhpParserErrors.expected(LBRACE));
        }
        if (builder.compareAndEat(keyword)) {
            do {
                // 'static' is a compile error, not a syntax error
                if (PhpNameParser.parseClassReference(builder, true) == null) {
                    builder.error(PhpParserErrors.expected("class reference"));
                    break;
                }
            }
            while (builder.compareAndEat(opCOMMA));
        }
        list.done(elementType);
    }

    private static void parseClassMember(PhpPsiBuilder builder) {
        PsiBuilder.Marker member = builder.mark();
        parseAttributes(builder);

        if (builder.compare(USE_KEYWORD)) {
            parseTraitUse(builder);
            member.done(PhpElementTypes.TRAIT_USE);
            return;
        }
        if (builder.compareAndEat(kwCASE)) {
            if (!PhpParserUtil.eatIdentifierLike(builder)) {
                builder.error(PhpParserErrors.expected("case name"));
            }
            if (builder.compareAndEat(opASGN)) {
                PhpExpressionParser.parseRequired(builder);
            }
            builder.match(opSEMICOLON);
            member.done(PhpElementTypes.ENUM_CASE);
            return;
        }

        PsiBuilder.Marker modifiers = builder.mark();
        parseMemberModifierTokens(builder);
        modifiers.done(PhpElementTypes.MODIFIER_LIST);

        if (builder.compareAndEat(kwCONST)) {
            parseClassConstants(builder);
            member.done(PhpElementTypes.CLASS_CONSTANTS);
            return;
        }
        if (builder.compare(kwFUNCTION)) {
            parseMethod(builder);
            member.done(PhpStubElements.CLASS_METHOD);
            return;
        }
        if (builder.compare(VARIABLE) || PhpNameParser.canStartType(builder)) {
            if (builder.compare(opQUEST) && builder.lookAhead(1) == STATIC_KEYWORD) {
                builder.error(PhpParserErrors.unexpected(STATIC_KEYWORD));
            }
            if (!builder.compare(VARIABLE)) {
                PhpNameParser.parseTypeDeclaration(builder, PhpElementTypes.FIELD_TYPE);
            }
            parseProperties(builder);
            member.done(PhpElementTypes.CLASS_FIELDS);
            return;
        }

        member.rollbackTo();
    }

    /**
     * {@code const int A = 1, B = 2;} - the type is optional (PHP 8.3)
     */
    private static void parseClassConstants(PhpPsiBuilder builder) {
        if (builder.lookAhead(1) != opASGN) {
            PhpNameParser.parseTypeDeclaration(builder, PhpElementTypes.TYPE_DECLARATION);
        }
        do {
            PsiBuilder.Marker constant = builder.mark();
            if (!PhpParserUtil.eatIdentifierLike(builder)) {
                constant.drop();
                builder.error(PhpParserErrors.expected("constant name"));
                break;
            }
            builder.match(opASGN);
            PhpExpressionParser.parseRequired(builder);
            constant.done(PhpElementTypes.CLASS_CONSTANT);
        }
        while (builder.compareAndEat(opCOMMA));
        builder.match(opSEMICOLON);
    }

    private static void parseMethod(PhpPsiBuilder builder) {
        builder.advanceLexer();
        parseIsReference(builder);
        if (!PhpParserUtil.eatIdentifierLike(builder)) {
            builder.error(PhpParserErrors.expected("method name"));
        }
        parseParameterList(builder);
        PhpNameParser.parseReturnTypeIfColon(builder);
        if (!builder.compareAndEat(opSEMICOLON)) {
            parseBody(builder);
        }
    }

    /**
     * {@code $a = 1, $b;} or {@code $a { get => ...; set { ... } }}
     */
    private static void parseProperties(PhpPsiBuilder builder) {
        PsiBuilder.Marker field;
        do {
            field = builder.mark();
            if (!builder.compareAndEat(VARIABLE)) {
                field.drop();
                builder.error(PhpParserErrors.expected("variable"));
                return;
            }
            if (builder.compareAndEat(opASGN)) {
                PhpExpressionParser.parseRequired(builder);
            }
            if (builder.compare(LBRACE)) {
                // PHP 8.4 hooks belong to their property
                parsePropertyHooks(builder);
                field.done(PhpElementTypes.CLASS_FIELD);
                return;
            }
            field.done(PhpElementTypes.CLASS_FIELD);
        }
        while (builder.compareAndEat(opCOMMA));
        builder.match(opSEMICOLON);
    }

    /**
     * PHP 8.4: {@code { #[A] final &get => expr; set(Type $value) { ... } }}
     */
    private static void parsePropertyHooks(PhpPsiBuilder builder) {
        PsiBuilder.Marker hooks = builder.mark();
        builder.advanceLexer();
        while (!builder.eof() && !builder.compare(RBRACE)) {
            PsiBuilder.Marker hook = builder.mark();
            parseAttributes(builder);
            PsiBuilder.Marker modifiers = builder.mark();
            parseMemberModifierTokens(builder);
            modifiers.done(PhpElementTypes.MODIFIER_LIST);
            builder.compareAndEat(opBIT_AND);
            if (!builder.compareAndEat(IDENTIFIER)) {
                hook.drop();
                builder.error(PhpParserErrors.expected("get or set"));
                break;
            }
            if (builder.compare(LPAREN)) {
                parseParameterList(builder);
            }
            if (builder.compareAndEat(HASH_ARRAY)) {
                PhpExpressionParser.parseRequired(builder);
                builder.match(opSEMICOLON);
            }
            else if (!builder.compareAndEat(opSEMICOLON)) {
                parseBody(builder);
            }
            hook.done(PhpElementTypes.PROPERTY_HOOK);
        }
        builder.match(RBRACE);
        hooks.done(PhpElementTypes.PROPERTY_HOOKS);
    }

    /**
     * {@code use A, B;} or {@code use A, B { A::x insteadof B; x as protected y; }}
     */
    private static void parseTraitUse(PhpPsiBuilder builder) {
        builder.advanceLexer();
        do {
            if (PhpNameParser.parseClassReference(builder, true) == null) {
                builder.error(PhpParserErrors.expected("trait name"));
                break;
            }
        }
        while (builder.compareAndEat(opCOMMA));

        if (!builder.compareAndEat(LBRACE)) {
            builder.match(opSEMICOLON);
            return;
        }
        while (!builder.eof() && !builder.compare(RBRACE)) {
            PsiBuilder.Marker adaptation = builder.mark();
            // [Trait::]method
            PsiBuilder.Marker traitName = builder.mark();
            if (PhpNameParser.parseClassReference(builder, true) != null && builder.compareAndEat(SCOPE_RESOLUTION)) {
                traitName.drop();
            }
            else {
                traitName.rollbackTo();
            }
            if (!PhpParserUtil.eatIdentifierLike(builder)) {
                adaptation.drop();
                builder.error(PhpParserErrors.expected("method name"));
                break;
            }
            if (builder.compareAndEat(INSTEADOF_KEYWORD)) {
                do {
                    PhpNameParser.parseClassReference(builder, true);
                }
                while (builder.compareAndEat(opCOMMA));
            }
            else if (builder.compareAndEat(kwAS)) {
                builder.compareAndEat(PhpParserUtil.VISIBILITY);
                if (!builder.compare(opSEMICOLON)) {
                    PhpParserUtil.eatIdentifierLike(builder);
                }
            }
            else {
                builder.error(PhpParserErrors.expected("insteadof or as"));
            }
            builder.match(opSEMICOLON);
            adaptation.done(PhpElementTypes.TRAIT_ADAPTATION);
        }
        builder.match(RBRACE);
    }
}
