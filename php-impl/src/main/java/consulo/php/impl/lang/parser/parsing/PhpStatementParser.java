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
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * PHP statements.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpStatementParser implements PhpTokenTypes {
    private static final TokenSet OPENING_TAGS = TokenSet.create(PHP_OPENING_TAG, PHP_ECHO_OPENING_TAG);

    private PhpStatementParser() {
    }

    public static void parseFile(PhpPsiBuilder builder) {
        parseStatementList(builder, TokenSet.EMPTY);
        while (!builder.eof()) {
            builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
            builder.advanceLexer();
        }
        checkUnterminatedComment(builder);
    }

    /**
     * An unclosed {@code /*} comment runs to the end of the file
     */
    private static void checkUnterminatedComment(PhpPsiBuilder builder) {
        IElementType last = builder.rawLookup(-1);
        if (last != C_STYLE_COMMENT && last != PhpDocElementTypes.DOC_COMMENT) {
            return;
        }
        CharSequence text = builder.getOriginalText().subSequence(builder.rawTokenTypeStart(-1), builder.getOriginalText().length());
        if (text.length() < 4 || !text.toString().endsWith("*/")) {
            builder.error("Unterminated comment");
        }
    }

    public static void parseStatementList(PhpPsiBuilder builder, TokenSet stopAt) {
        PsiBuilder.Marker list = builder.mark();
        while (!builder.eof() && !builder.compare(stopAt)) {
            int offset = builder.getCurrentOffset();
            parseStatement(builder);
            if (offset == builder.getCurrentOffset()) {
                builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
                builder.advanceLexer();
            }
        }
        list.done(PhpElementTypes.GROUP_STATEMENT);
    }

    /**
     * A single statement: body of {@code if}, {@code while}, ... and items of statement lists
     */
    public static void parseStatement(PhpPsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token == null) {
            return;
        }

        if (OPENING_TAGS.contains(token) || token == PHP_CLOSING_TAG || token == INLINE_DATA) {
            builder.advanceLexer();
            return;
        }
        if (token == HTML) {
            PsiBuilder.Marker html = builder.mark();
            builder.advanceLexer();
            html.done(PhpElementTypes.HTML);
            return;
        }
        if (token == LBRACE) {
            builder.advanceLexer();
            parseStatementList(builder, TokenSet.create(RBRACE));
            builder.match(RBRACE);
            return;
        }
        if (token == opSEMICOLON) {
            PsiBuilder.Marker statement = builder.mark();
            builder.advanceLexer();
            statement.done(PhpElementTypes.STATEMENT);
            return;
        }

        if (PhpDeclarationParser.isFunctionDeclarationStart(builder) || token == ATTRIBUTE_START && isAttributedFunction(builder)) {
            PhpDeclarationParser.parseFunction(builder);
            return;
        }
        if (PhpDeclarationParser.isClassStart(builder)) {
            PhpDeclarationParser.parseClass(builder);
            return;
        }
        if (token == ATTRIBUTE_START && isAttributedConstant(builder)) {
            parseConstants(builder);
            return;
        }

        if (token == NAMESPACE_KEYWORD && builder.lookAhead(1) != SLASH) {
            parseNamespace(builder);
        }
        else if (token == USE_KEYWORD) {
            parseUse(builder);
        }
        else if (token == kwCONST) {
            parseConstants(builder);
        }
        else if (token == kwGLOBAL) {
            parseGlobal(builder);
        }
        else if (token == kwIF) {
            parseIf(builder);
        }
        else if (token == kwWHILE) {
            parseWhile(builder);
        }
        else if (token == kwDO) {
            parseDoWhile(builder);
        }
        else if (token == kwFOR) {
            parseFor(builder);
        }
        else if (token == kwFOREACH) {
            parseForeach(builder);
        }
        else if (token == kwSWITCH) {
            parseSwitch(builder);
        }
        else if (token == kwBREAK) {
            parseKeywordWithOptionalExpression(builder, PhpElementTypes.BREAK);
        }
        else if (token == kwCONTINUE) {
            parseKeywordWithOptionalExpression(builder, PhpElementTypes.CONTINUE);
        }
        else if (token == kwRETURN) {
            parseKeywordWithOptionalExpression(builder, PhpElementTypes.RETURN);
        }
        else if (token == kwTHROW) {
            parseKeywordWithOptionalExpression(builder, PhpElementTypes.THROW);
        }
        else if (token == kwECHO) {
            parseExpressionList(builder, PhpElementTypes.ECHO);
        }
        else if (token == STATIC_KEYWORD && builder.lookAhead(1) == VARIABLE) {
            parseStaticVariables(builder);
        }
        else if (token == kwUNSET) {
            parseUnset(builder);
        }
        else if (token == kwDECLARE) {
            parseDeclare(builder);
        }
        else if (token == kwTRY) {
            parseTry(builder);
        }
        else if (token == kwGOTO) {
            PsiBuilder.Marker statement = builder.mark();
            builder.advanceLexer();
            builder.match(IDENTIFIER);
            PhpParserUtil.expectStatementEnd(builder);
            statement.done(PhpElementTypes.GOTO);
        }
        else if (token == HALT_COMPILER_KEYWORD) {
            PsiBuilder.Marker statement = builder.mark();
            builder.advanceLexer();
            builder.match(LPAREN);
            builder.match(RPAREN);
            PhpParserUtil.expectStatementEnd(builder);
            statement.done(PhpElementTypes.HALT_COMPILER);
        }
        else if (token == IDENTIFIER && builder.lookAhead(1) == opCOLON) {
            PsiBuilder.Marker label = builder.mark();
            builder.advanceLexer();
            builder.advanceLexer();
            label.done(PhpElementTypes.LABEL);
        }
        else if (PhpExpressionParser.parseStatementExpression(builder) != PhpElementTypes.EMPTY_INPUT) {
            PhpParserUtil.expectStatementEnd(builder);
        }
    }

    private static boolean isAttributedConstant(PhpPsiBuilder builder) {
        PsiBuilder.Marker probe = builder.mark();
        PhpDeclarationParser.parseAttributes(builder);
        boolean result = builder.compare(kwCONST);
        probe.rollbackTo();
        return result;
    }

    private static boolean isAttributedFunction(PhpPsiBuilder builder) {
        PsiBuilder.Marker probe = builder.mark();
        PhpDeclarationParser.parseAttributes(builder);
        boolean result = PhpDeclarationParser.isFunctionDeclarationStart(builder);
        probe.rollbackTo();
        return result;
    }

    /**
     * Statement body or {@code ':' statements endXXX ';'}
     */
    private static void parseBody(PhpPsiBuilder builder, IElementType endKeyword) {
        if (builder.compareAndEat(opCOLON)) {
            parseStatementList(builder, TokenSet.create(endKeyword));
            builder.match(endKeyword);
            PhpParserUtil.expectStatementEnd(builder);
        }
        else {
            parseStatement(builder);
        }
    }

    private static void parseCondition(PhpPsiBuilder builder) {
        builder.match(LPAREN);
        PhpExpressionParser.parseRequired(builder);
        builder.match(RPAREN);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------

    private static void parseNamespace(PhpPsiBuilder builder) {
        PsiBuilder.Marker namespace = builder.mark();
        builder.advanceLexer();
        if (builder.compare(NAMESPACE_KEYWORD) && PhpParserUtil.isRawNext(builder, SLASH)) {
            builder.error(PhpParserErrors.expected(LBRACE));
        }
        if (PhpParserUtil.isIdentifierLike(builder) && !builder.compare(LBRACE)) {
            PsiBuilder.Marker name = builder.mark();
            PhpParserUtil.eatIdentifierLike(builder);
            while (builder.compare(SLASH) && PhpParserUtil.isIdentifierLike(builder.rawLookup(1))) {
                builder.advanceLexer();
                PhpParserUtil.eatIdentifierLike(builder);
            }
            name.collapse(IDENTIFIER);
        }
        if (builder.compareAndEat(LBRACE)) {
            parseStatementList(builder, TokenSet.create(RBRACE));
            builder.match(RBRACE);
        }
        else {
            PhpParserUtil.expectStatementEnd(builder);
            // statements up to the next 'namespace X;' - but not 'namespace\foo()'
            PsiBuilder.Marker list = builder.mark();
            while (!builder.eof() && !builder.compare(RBRACE) && !(builder.compare(NAMESPACE_KEYWORD) && builder.lookAhead(1) != SLASH)) {
                int offset = builder.getCurrentOffset();
                parseStatement(builder);
                if (offset == builder.getCurrentOffset()) {
                    builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
                    builder.advanceLexer();
                }
            }
            list.done(PhpElementTypes.GROUP_STATEMENT);
        }
        namespace.done(PhpElementTypes.NAMESPACE);
    }

    /**
     * {@code use A\B as C, D;}, {@code use function A\b;}, {@code use const A\B;}, {@code use A\{B, function c, const D as E};}
     */
    private static void parseUse(PhpPsiBuilder builder) {
        PsiBuilder.Marker list = builder.mark();
        builder.advanceLexer();
        boolean kind = eatUseKind(builder);
        do {
            if (!parseUseClause(builder, kind)) {
                break;
            }
        }
        while (builder.compareAndEat(opCOMMA));
        PhpParserUtil.expectStatementEnd(builder);
        list.done(PhpElementTypes.USE_LIST);
    }

    private static boolean eatUseKind(PhpPsiBuilder builder) {
        return builder.compareAndEat(kwFUNCTION) || builder.compareAndEat(kwCONST);
    }

    private static boolean parseUseClause(PhpPsiBuilder builder, boolean kind) {
        PsiBuilder.Marker use = builder.mark();
        if (PhpNameParser.parseClassReference(builder, false) == null) {
            use.drop();
            builder.error("Reference expected");
            return false;
        }
        if (builder.compare(SLASH) && builder.lookAhead(1) == LBRACE) {
            // group use
            builder.advanceLexer();
            builder.advanceLexer();
            use.drop();
            if (builder.compare(RBRACE)) {
                builder.error(PhpParserErrors.expected(IDENTIFIER));
            }
            while (!builder.eof() && !builder.compare(RBRACE)) {
                PsiBuilder.Marker item = builder.mark();
                if (eatUseKind(builder) && kind) {
                    builder.error(PhpParserErrors.expected(RBRACE));
                }
                if (builder.compare(SLASH)) {
                    builder.error(PhpParserErrors.expected(IDENTIFIER));
                }
                if (PhpNameParser.parseClassReference(builder, false) == null) {
                    item.drop();
                    builder.error("Reference expected");
                    break;
                }
                parseUseAlias(builder);
                item.done(PhpElementTypes.USE);
                if (!builder.compareAndEat(opCOMMA)) {
                    break;
                }
            }
            builder.match(RBRACE);
            return true;
        }
        parseUseAlias(builder);
        use.done(PhpElementTypes.USE);
        return true;
    }

    private static void parseUseAlias(PhpPsiBuilder builder) {
        if (builder.compareAndEat(kwAS)) {
            if (PhpParserUtil.isContextual(builder, "readonly") || !builder.compareAndEat(IDENTIFIER)) {
                builder.error(PhpParserErrors.expected(IDENTIFIER));
                PhpParserUtil.eatIdentifierLike(builder);
            }
        }
    }

    /**
     * {@code const A = 1, B = 2;} outside of classes
     */
    private static void parseConstants(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        PhpDeclarationParser.parseAttributes(builder);
        if (builder.isInFunction()) {
            builder.error(PhpParserErrors.unexpected(kwCONST));
        }
        builder.match(kwCONST);
        do {
            PsiBuilder.Marker constant = builder.mark();
            if (!builder.compareAndEat(IDENTIFIER)) {
                constant.drop();
                builder.error(PhpParserErrors.expected("constant name"));
                break;
            }
            builder.match(opASGN);
            PhpExpressionParser.parseRequired(builder);
            constant.done(PhpElementTypes.CONSTANT_ITEM);
        }
        while (builder.compareAndEat(opCOMMA));
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(PhpElementTypes.CONSTANT_DECLARATION);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // control flow

    private static void parseIf(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        parseCondition(builder);
        if (builder.compareAndEat(opCOLON)) {
            TokenSet stop = TokenSet.create(kwELSEIF, kwELSE, kwENDIF);
            parseStatementList(builder, stop);
            while (builder.compare(kwELSEIF)) {
                PsiBuilder.Marker elseIf = builder.mark();
                builder.advanceLexer();
                parseCondition(builder);
                builder.match(opCOLON);
                parseStatementList(builder, stop);
                elseIf.done(PhpElementTypes.ELSE_IF);
            }
            if (builder.compare(kwELSE)) {
                PsiBuilder.Marker elseClause = builder.mark();
                builder.advanceLexer();
                builder.match(opCOLON);
                parseStatementList(builder, TokenSet.create(kwENDIF));
                elseClause.done(PhpElementTypes.ELSE);
            }
            builder.match(kwENDIF);
            PhpParserUtil.expectStatementEnd(builder);
        }
        else {
            parseStatement(builder);
            while (builder.compare(kwELSEIF)) {
                PsiBuilder.Marker elseIf = builder.mark();
                builder.advanceLexer();
                parseCondition(builder);
                parseStatement(builder);
                elseIf.done(PhpElementTypes.ELSE_IF);
            }
            if (builder.compare(kwELSE)) {
                PsiBuilder.Marker elseClause = builder.mark();
                builder.advanceLexer();
                parseStatement(builder);
                elseClause.done(PhpElementTypes.ELSE);
            }
        }
        statement.done(PhpElementTypes.IF);
    }

    private static void parseWhile(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        parseCondition(builder);
        parseBody(builder, kwENDWHILE);
        statement.done(PhpElementTypes.WHILE);
    }

    private static void parseDoWhile(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        parseStatement(builder);
        builder.match(kwWHILE);
        parseCondition(builder);
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(PhpElementTypes.DO_WHILE);
    }

    private static void parseFor(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        builder.match(LPAREN);
        parseForExpressions(builder, opSEMICOLON, false);
        builder.match(opSEMICOLON);
        parseForExpressions(builder, opSEMICOLON, true);
        builder.match(opSEMICOLON);
        parseForExpressions(builder, RPAREN, false);
        builder.match(RPAREN);
        parseBody(builder, kwENDFOR);
        statement.done(PhpElementTypes.FOR);
    }

    /**
     * @param condition the last expression of a condition list is its value, so it can not be a {@code (void)} cast
     */
    private static void parseForExpressions(PhpPsiBuilder builder, IElementType end, boolean condition) {
        if (builder.compare(end)) {
            return;
        }
        boolean lastIsVoidCast;
        do {
            lastIsVoidCast = builder.compare(opVOID_CAST);
            PhpParserUtil.expectExpression(builder, PhpExpressionParser.parseStatementExpression(builder));
        }
        while (builder.compareAndEat(opCOMMA));
        if (condition && lastIsVoidCast) {
            builder.error(PhpParserErrors.expected(opCOMMA));
        }
    }

    private static void parseForeach(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        builder.match(LPAREN);
        PhpExpressionParser.parseRequired(builder);
        builder.match(kwAS);
        parseForeachVariable(builder);
        if (builder.compareAndEat(HASH_ARRAY)) {
            parseForeachVariable(builder);
        }
        builder.match(RPAREN);
        parseBody(builder, kwENDFOREACH);
        statement.done(PhpElementTypes.FOREACH);
    }

    private static void parseForeachVariable(PhpPsiBuilder builder) {
        PsiBuilder.Marker isReference = builder.mark();
        builder.compareAndEat(opBIT_AND);
        isReference.done(PhpElementTypes.IS_REFERENCE);
        PhpParserUtil.expectExpression(builder, PhpExpressionParser.parseDestructuringTarget(builder));
    }

    private static void parseSwitch(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        parseCondition(builder);
        IElementType end;
        if (builder.compareAndEat(opCOLON)) {
            end = kwENDSWITCH;
        }
        else {
            builder.match(LBRACE);
            end = RBRACE;
        }
        builder.compareAndEat(opSEMICOLON);

        TokenSet stop = TokenSet.create(end, kwCASE, kwDEFAULT);
        while (!builder.eof() && !builder.compare(end)) {
            PsiBuilder.Marker caseClause = builder.mark();
            IElementType type;
            if (builder.compareAndEat(kwDEFAULT)) {
                type = PhpElementTypes.CASE_DEFAULT;
            }
            else if (builder.compareAndEat(kwCASE)) {
                PhpExpressionParser.parseRequired(builder);
                type = PhpElementTypes.CASE;
            }
            else {
                caseClause.drop();
                builder.error(PhpParserErrors.unexpected(builder.getTokenType()));
                builder.advanceLexer();
                continue;
            }
            builder.match(TokenSet.create(opCOLON, opSEMICOLON));
            parseStatementList(builder, stop);
            caseClause.done(type);
        }
        builder.match(end);
        if (end == kwENDSWITCH) {
            PhpParserUtil.expectStatementEnd(builder);
        }
        statement.done(PhpElementTypes.SWITCH);
    }

    private static void parseKeywordWithOptionalExpression(PhpPsiBuilder builder, IElementType elementType) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        if (!builder.compare(opSEMICOLON) && !builder.compare(PHP_CLOSING_TAG)) {
            PhpExpressionParser.parseRequired(builder);
        }
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(elementType);
    }

    /**
     * {@code echo a, b;} and {@code global $a, $b;}
     */
    private static void parseExpressionList(PhpPsiBuilder builder, IElementType elementType) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        do {
            PhpExpressionParser.parseRequired(builder);
        }
        while (builder.compareAndEat(opCOMMA));
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(elementType);
    }

    /**
     * {@code global $a, $$b, ${'c'};} - simple variables only
     */
    private static void parseGlobal(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        do {
            if (!builder.compare(VARIABLE) && !builder.compare(DOLLAR)) {
                builder.error(PhpParserErrors.expected("variable"));
                break;
            }
            PhpExpressionParser.parseSimpleVariable(builder);
        }
        while (builder.compareAndEat(opCOMMA));
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(PhpElementTypes.GLOBAL);
    }

    private static void parseStaticVariables(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        do {
            PsiBuilder.Marker variable = builder.mark();
            if (!builder.compareAndEat(VARIABLE)) {
                variable.drop();
                builder.error(PhpParserErrors.expected("variable"));
                break;
            }
            if (builder.compareAndEat(opASGN)) {
                PhpExpressionParser.parseRequired(builder);
            }
            variable.done(PhpElementTypes.VARIABLE_REFERENCE);
        }
        while (builder.compareAndEat(opCOMMA));
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(PhpElementTypes.STATIC);
    }

    private static void parseUnset(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        builder.match(LPAREN);
        while (!builder.eof() && !builder.compare(RPAREN)) {
            IElementType result = PhpExpressionParser.parseRequired(builder);
            if (result != PhpElementTypes.EMPTY_INPUT && !PhpExpressionParser.VARIABLES.contains(result)) {
                builder.error(PhpParserErrors.expected("variable"));
            }
            if (!builder.compareAndEat(opCOMMA)) {
                break;
            }
        }
        builder.match(RPAREN);
        PhpParserUtil.expectStatementEnd(builder);
        statement.done(PhpElementTypes.UNSET);
    }

    private static void parseDeclare(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        builder.match(LPAREN);
        do {
            PsiBuilder.Marker directive = builder.mark();
            if (!builder.compareAndEat(IDENTIFIER)) {
                directive.drop();
                builder.error(PhpParserErrors.expected("directive"));
                break;
            }
            builder.match(opASGN);
            PhpExpressionParser.parseRequired(builder);
            directive.done(PhpElementTypes.DECLARE_DIRECTIVE);
        }
        while (builder.compareAndEat(opCOMMA));
        builder.match(RPAREN);
        if (builder.compare(opSEMICOLON) || builder.compare(PHP_CLOSING_TAG)) {
            PhpParserUtil.expectStatementEnd(builder);
        }
        else {
            parseBody(builder, kwENDDECLARE);
        }
        statement.done(PhpElementTypes.DECLARE);
    }

    private static void parseTry(PhpPsiBuilder builder) {
        PsiBuilder.Marker statement = builder.mark();
        builder.advanceLexer();
        parseBlock(builder);
        while (builder.compare(kwCATCH)) {
            PsiBuilder.Marker catchClause = builder.mark();
            builder.advanceLexer();
            builder.match(LPAREN);
            // catch (A | B $e) - the variable is optional since PHP 8
            do {
                if (PhpNameParser.parseClassReference(builder, true) == null) {
                    builder.error(PhpParserErrors.expected("exception class"));
                    break;
                }
            }
            while (builder.compareAndEat(opBIT_OR));
            if (builder.compare(VARIABLE)) {
                PsiBuilder.Marker variable = builder.mark();
                builder.advanceLexer();
                variable.done(PhpElementTypes.VARIABLE_REFERENCE);
            }
            builder.match(RPAREN);
            parseBlock(builder);
            catchClause.done(PhpElementTypes.CATCH);
        }
        if (builder.compare(FINALLY_KEYWORD)) {
            PsiBuilder.Marker finallyClause = builder.mark();
            builder.advanceLexer();
            parseBlock(builder);
            finallyClause.done(PhpElementTypes.FINALLY_STATEMENT);
        }
        statement.done(PhpElementTypes.TRY);
    }

    private static void parseBlock(PhpPsiBuilder builder) {
        builder.match(LBRACE);
        parseStatementList(builder, TokenSet.create(RBRACE));
        builder.match(RBRACE);
    }
}
