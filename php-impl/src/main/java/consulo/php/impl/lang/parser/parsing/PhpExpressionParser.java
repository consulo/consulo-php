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
import consulo.php.lang.lexer.PhpTokenTypes;

import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * PHP 8.5 expressions: precedence climbing for binary operators, prefix operators, primary expressions and dereference chains.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpExpressionParser implements PhpTokenTypes {
    // https://www.php.net/manual/en/language.operators.precedence.php - higher binds tighter
    private static final int P_LIT_OR = 1;
    private static final int P_LIT_XOR = 2;
    private static final int P_LIT_AND = 3;
    private static final int P_ASSIGN = 4;
    private static final int P_TERNARY = 5;
    private static final int P_COALESCE = 6;
    private static final int P_OR = 7;
    private static final int P_AND = 8;
    private static final int P_BIT_OR = 9;
    private static final int P_BIT_XOR = 10;
    private static final int P_BIT_AND = 11;
    private static final int P_EQUALITY = 12;
    private static final int P_RELATIONAL = 13;
    private static final int P_PIPE = 14;
    private static final int P_CONCAT = 15;
    private static final int P_SHIFT = 16;
    private static final int P_ADDITIVE = 17;
    private static final int P_MULTIPLICATIVE = 18;
    private static final int P_INSTANCEOF = 20;
    private static final int P_POW = 22;

    private record BinaryOperator(int precedence, boolean rightAssociative, IElementType elementType) {
    }

    private static final Map<IElementType, BinaryOperator> BINARY_OPERATORS = new HashMap<>();

    static {
        binary(P_LIT_OR, false, PhpElementTypes.LITERAL_LOGICAL_EXPRESSION, opLIT_OR);
        binary(P_LIT_XOR, false, PhpElementTypes.LITERAL_LOGICAL_EXPRESSION, opLIT_XOR);
        binary(P_LIT_AND, false, PhpElementTypes.LITERAL_LOGICAL_EXPRESSION, opLIT_AND);
        binary(P_COALESCE, true, PhpElementTypes.COALESCE_EXPRESSION, opCOALESCE);
        binary(P_OR, false, PhpElementTypes.LOGICAL_EXPRESSION, opOR);
        binary(P_AND, false, PhpElementTypes.LOGICAL_EXPRESSION, opAND);
        binary(P_BIT_OR, false, PhpElementTypes.BIT_EXPRESSION, opBIT_OR);
        binary(P_BIT_XOR, false, PhpElementTypes.BIT_EXPRESSION, opBIT_XOR);
        binary(P_BIT_AND, false, PhpElementTypes.BIT_EXPRESSION, opBIT_AND);
        binary(P_EQUALITY, false, PhpElementTypes.EQUALITY_EXPRESSION, opEQUAL, opNOT_EQUAL, opIDENTICAL, opNOT_IDENTICAL, opSPACESHIP);
        binary(P_RELATIONAL, false, PhpElementTypes.RELATIONAL_EXPRESSION, opLESS, opLESS_OR_EQUAL, opGREATER, opGREATER_OR_EQUAL);
        binary(P_PIPE, false, PhpElementTypes.PIPE_EXPRESSION, opPIPE);
        binary(P_CONCAT, false, PhpElementTypes.CONCATENATION_EXPRESSION, opCONCAT);
        binary(P_SHIFT, false, PhpElementTypes.SHIFT_EXPRESSION, opSHIFT_LEFT, opSHIFT_RIGHT);
        binary(P_ADDITIVE, false, PhpElementTypes.ADDITIVE_EXPRESSION, opPLUS, opMINUS);
        binary(P_MULTIPLICATIVE, false, PhpElementTypes.MULTIPLICATIVE_EXPRESSION, opMUL, opDIV, opREM);
        binary(P_INSTANCEOF, false, PhpElementTypes.INSTANCEOF_EXPRESSION, kwINSTANCEOF);
        binary(P_POW, true, PhpElementTypes.POW_EXPRESSION, opPOW);
    }

    private static void binary(int precedence, boolean rightAssociative, IElementType elementType, IElementType... tokens) {
        for (IElementType token : tokens) {
            BINARY_OPERATORS.put(token, new BinaryOperator(precedence, rightAssociative, elementType));
        }
    }

    public static final TokenSet ASSIGNABLE = TokenSet.create(PhpElementTypes.VARIABLE_REFERENCE, PhpElementTypes.FIELD_REFERENCE,
        PhpElementTypes.ARRAY_ACCESS_EXPRESSION, PhpElementTypes.ARRAY_CREATION_EXPRESSION, PhpElementTypes.FUNCTION_REFERENCE,
        PhpElementTypes.METHOD_REFERENCE);

    private static final TokenSet DEREFERENCE = TokenSet.create(ARROW, NULLSAFE_ARROW, SCOPE_RESOLUTION, LBRACKET);

    /**
     * PHP's {@code variable} production: what {@code unset()} and {@code =&} accept
     */
    public static final TokenSet VARIABLES = TokenSet.create(PhpElementTypes.VARIABLE_REFERENCE, PhpElementTypes.FIELD_REFERENCE,
        PhpElementTypes.ARRAY_ACCESS_EXPRESSION, PhpElementTypes.FUNCTION_REFERENCE, PhpElementTypes.METHOD_REFERENCE,
        PhpElementTypes.ASSIGNMENT_EXPRESSION);

    private static final TokenSet INCLUDES = TokenSet.create(kwINCLUDE, kwINCLUDE_ONCE, kwREQUIRE, kwREQUIRE_ONCE);

    private static final TokenSet UNARY_ARITHMETIC = TokenSet.create(opPLUS, opMINUS, opBIT_NOT);

    private static final TokenSet MEMBER_ACCESS = TokenSet.create(ARROW, NULLSAFE_ARROW);

    private PhpExpressionParser() {
    }

    public static IElementType parse(PhpPsiBuilder builder) {
        return parse(builder, 0);
    }

    public static IElementType parseRequired(PhpPsiBuilder builder) {
        IElementType result = parse(builder, 0);
        PhpParserUtil.expectExpression(builder, result);
        return result;
    }

    private static IElementType parseRequired(PhpPsiBuilder builder, int minPrecedence) {
        IElementType result = parse(builder, minPrecedence);
        PhpParserUtil.expectExpression(builder, result);
        return result;
    }

    public static IElementType parse(PhpPsiBuilder builder, int minPrecedence) {
        PsiBuilder.Marker left = builder.mark();
        IElementType result = parseUnary(builder);
        if (result == PhpElementTypes.EMPTY_INPUT) {
            left.drop();
            return result;
        }
        return parseBinary(builder, left, result, minPrecedence);
    }

    /**
     * Binary and ternary operators applied to the already parsed left operand, which starts at the {@code left} marker
     */
    private static IElementType parseBinary(PhpPsiBuilder builder, PsiBuilder.Marker left, IElementType leftResult, int minPrecedence) {
        IElementType result = leftResult;
        while (true) {
            IElementType token = builder.getTokenType();
            if (token == opQUEST || token == ELVIS) {
                if (P_TERNARY < minPrecedence) {
                    break;
                }
                builder.advanceLexer();
                if (token == opQUEST && !builder.compare(opCOLON)) {
                    parseRequired(builder, P_ASSIGN);
                    builder.match(opCOLON);
                    parseRequired(builder, P_TERNARY + 1);
                    result = PhpElementTypes.TERNARY_EXPRESSION;
                }
                else {
                    // '?:' or '? :'
                    builder.compareAndEat(opCOLON);
                    parseRequired(builder, P_TERNARY + 1);
                    result = PhpElementTypes.ELVIS_EXPRESSION;
                }
                left.done(result);
                left = left.precede();
                continue;
            }

            BinaryOperator operator = token == null ? null : BINARY_OPERATORS.get(token);
            if (operator == null || operator.precedence() < minPrecedence) {
                break;
            }
            builder.advanceLexer();
            if (token == kwINSTANCEOF) {
                parseInstanceofTarget(builder);
            }
            else {
                parseRequired(builder, operator.rightAssociative() ? operator.precedence() : operator.precedence() + 1);
            }
            result = operator.elementType();
            left.done(result);
            left = left.precede();
        }
        left.drop();
        return result;
    }

    private static void parseInstanceofTarget(PhpPsiBuilder builder) {
        if (PhpNameParser.canStartName(builder) || builder.compare(STATIC_KEYWORD)) {
            PhpNameParser.parseClassReference(builder, true);
            return;
        }
        // PHP 8: any expression ('$a instanceof $b', 'instanceof (expr)')
        parseRequired(builder, P_INSTANCEOF + 1);
    }

    private record PrefixOperator(PsiBuilder.Marker marker, IElementType elementType, int operandPrecedence) {
    }

    private static @Nullable PrefixOperator prefixOperator(PhpPsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token == opNOT) {
            return new PrefixOperator(builder.mark(), PhpElementTypes.UNARY_EXPRESSION, P_INSTANCEOF);
        }
        if (UNARY_ARITHMETIC.contains(token)) {
            return new PrefixOperator(builder.mark(), PhpElementTypes.UNARY_EXPRESSION, P_POW);
        }
        if (token == opVOID_CAST) {
            // PHP 8.5: '(void)' is a statement, not an expression
            builder.error(PhpParserErrors.unexpected(token));
            return new PrefixOperator(builder.mark(), PhpElementTypes.TYPE_CAST_EXPRESSION, P_POW);
        }
        if (token != null && tsCAST_OPS.contains(token)) {
            return new PrefixOperator(builder.mark(), PhpElementTypes.TYPE_CAST_EXPRESSION, P_POW);
        }
        if (token == opSILENCE) {
            return new PrefixOperator(builder.mark(), PhpElementTypes.SILENCE_EXPRESSION, P_POW);
        }
        return null;
    }

    /**
     * Prefix operators are collected iteratively - '~~~~...$a' must not overflow the stack
     */
    private static IElementType parsePrefixOperators(PhpPsiBuilder builder) {
        Deque<PrefixOperator> operators = new ArrayDeque<>();
        PrefixOperator operator;
        while ((operator = prefixOperator(builder)) != null) {
            operators.push(operator);
            builder.advanceLexer();
        }

        PsiBuilder.Marker operand = builder.mark();
        IElementType result = parseUnary(builder);
        if (result == PhpElementTypes.EMPTY_INPUT) {
            operand.drop();
            builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
        }
        else {
            result = parseBinary(builder, operand, result, operators.peek().operandPrecedence());
        }

        while (!operators.isEmpty()) {
            PrefixOperator current = operators.pop();
            current.marker().done(current.elementType());
            result = current.elementType();
            PrefixOperator outer = operators.peek();
            if (outer != null) {
                result = parseBinary(builder, current.marker().precede(), result, outer.operandPrecedence());
            }
        }
        return result;
    }

    private static IElementType parseUnary(PhpPsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token == null) {
            return PhpElementTypes.EMPTY_INPUT;
        }

        if (token == opNOT || UNARY_ARITHMETIC.contains(token) || tsCAST_OPS.contains(token) || token == opSILENCE) {
            return parsePrefixOperators(builder);
        }
        if (token == opINCREMENT || token == opDECREMENT) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            PhpParserUtil.expectExpression(builder, parsePostfix(builder, false));
            marker.done(PhpElementTypes.UNARY_EXPRESSION);
            return PhpElementTypes.UNARY_EXPRESSION;
        }
        if (token == kwPRINT) {
            return prefix(builder, P_ASSIGN, PhpElementTypes.PRINT_EXPRESSION);
        }
        if (INCLUDES.contains(token)) {
            return prefix(builder, P_ASSIGN, PhpElementTypes.INCLUDE_EXPRESSION);
        }
        if (token == kwTHROW) {
            return prefix(builder, 0, PhpElementTypes.THROW_EXPRESSION);
        }
        if (token == YIELD_KEYWORD) {
            return parseYield(builder);
        }
        if (token == kwCLONE) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            if (builder.compare(LPAREN)) {
                // PHP 8.5: clone($object, [...])
                parseArguments(builder);
                parsePostfixChain(builder, marker, PhpElementTypes.CLONE_EXPRESSION);
                return PhpElementTypes.CLONE_EXPRESSION;
            }
            PhpParserUtil.expectExpression(builder, parseUnary(builder));
            marker.done(PhpElementTypes.CLONE_EXPRESSION);
            return PhpElementTypes.CLONE_EXPRESSION;
        }

        return parsePostfix(builder, true);
    }

    private static IElementType prefix(PhpPsiBuilder builder, int operandPrecedence, IElementType elementType) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        parseRequired(builder, operandPrecedence);
        marker.done(elementType);
        return elementType;
    }

    private static IElementType parseYield(PhpPsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        if (PhpParserUtil.isContextual(builder, "from")) {
            builder.remapCurrentToken(FROM_KEYWORD);
            builder.advanceLexer();
            parseRequired(builder, P_ASSIGN);
        }
        else if (parse(builder, P_ASSIGN) != PhpElementTypes.EMPTY_INPUT && builder.compareAndEat(HASH_ARRAY)) {
            parseRequired(builder, P_ASSIGN);
        }
        marker.done(PhpElementTypes.YIELD);
        return PhpElementTypes.YIELD;
    }

    /**
     * Primary expression with its dereference chain, optionally followed by an assignment
     */
    private static IElementType parsePostfix(PhpPsiBuilder builder, boolean allowAssignment) {
        boolean isList = builder.compare(kwLIST);
        PsiBuilder.Marker assignment = builder.mark();
        PsiBuilder.Marker chain = builder.mark();
        IElementType result = parsePrimary(builder);
        if (result == PhpElementTypes.EMPTY_INPUT) {
            chain.drop();
            assignment.drop();
            return result;
        }
        result = parsePostfixChain(builder, chain, result);

        if (!allowAssignment || !builder.compare(tsASGN_OPS)) {
            if (isList) {
                builder.error(PhpParserErrors.expected(opASGN));
            }
            assignment.drop();
            return result;
        }

        // assignment binds to the variable on its left regardless of precedence: '!$a = f()', '$a && $b = 1'
        boolean simple = builder.compare(opASGN);
        if (!ASSIGNABLE.contains(result)) {
            builder.error("Expression is not assignable");
        }
        builder.advanceLexer();
        if (simple && builder.compareAndEat(opBIT_AND)) {
            // '$a = &$b' binds to the variable only: '$a = &$b + 1' is '($a = &$b) + 1'
            IElementType reference = parsePostfix(builder, true);
            if (reference == PhpElementTypes.EMPTY_INPUT) {
                builder.error(PhpParserErrors.expected("variable"));
            }
            else if (!VARIABLES.contains(reference)) {
                builder.error(PhpParserErrors.expected("variable"));
            }
        }
        else {
            parseRequired(builder, P_ASSIGN);
        }

        IElementType type;
        if (!simple) {
            type = PhpElementTypes.SELF_ASSIGNMENT_EXPRESSION;
        }
        else if (result == PhpElementTypes.ARRAY_CREATION_EXPRESSION) {
            type = PhpElementTypes.MULTIASSIGNMENT_EXPRESSION;
        }
        else {
            type = PhpElementTypes.ASSIGNMENT_EXPRESSION;
        }
        assignment.done(type);
        return type;
    }

    /**
     * {@code ->name}, {@code ?->name}, {@code ::name}, {@code [index]}, {@code (args)} and postfix {@code ++}/{@code --}.
     * The marker must start at the qualifier and is completed or dropped.
     */
    private static IElementType parsePostfixChain(PhpPsiBuilder builder, PsiBuilder.Marker marker, IElementType qualifier) {
        IElementType result = qualifier;
        if (qualifier == PhpElementTypes.CLONE_EXPRESSION) {
            // marker covers 'clone(...)' itself
            marker.done(qualifier);
            marker = marker.precede();
        }

        while (true) {
            IElementType token = builder.getTokenType();
            if (token == LBRACKET) {
                builder.advanceLexer();
                if (!builder.compare(RBRACKET)) {
                    PsiBuilder.Marker index = builder.mark();
                    parseRequired(builder);
                    index.done(PhpElementTypes.ARRAY_INDEX);
                }
                builder.match(RBRACKET);
                result = PhpElementTypes.ARRAY_ACCESS_EXPRESSION;
            }
            else if (MEMBER_ACCESS.contains(token)) {
                builder.advanceLexer();
                parseMemberName(builder);
                if (builder.compare(LPAREN)) {
                    parseArguments(builder);
                    result = PhpElementTypes.METHOD_REFERENCE;
                }
                else {
                    result = PhpElementTypes.FIELD_REFERENCE;
                }
            }
            else if (token == SCOPE_RESOLUTION) {
                builder.advanceLexer();
                result = parseStaticMember(builder);
            }
            else if (token == LPAREN) {
                parseArguments(builder);
                result = PhpElementTypes.FUNCTION_REFERENCE;
            }
            else if (token == opINCREMENT || token == opDECREMENT) {
                builder.advanceLexer();
                marker.done(PhpElementTypes.POSTFIX_EXPRESSION);
                return PhpElementTypes.POSTFIX_EXPRESSION;
            }
            else {
                break;
            }
            marker.done(result);
            marker = marker.precede();
        }
        marker.drop();
        return result;
    }

    /**
     * After {@code ->}: {@code name}, {@code $name}, {@code {expr}}
     */
    private static void parseMemberName(PhpPsiBuilder builder) {
        if (PhpParserUtil.eatIdentifierLike(builder)) {
            return;
        }
        if (builder.compare(VARIABLE) || builder.compare(DOLLAR)) {
            parseSimpleVariable(builder);
            return;
        }
        if (builder.compareAndEat(LBRACE)) {
            parseRequired(builder);
            builder.match(RBRACE);
            return;
        }
        builder.error(PhpParserErrors.expected("member name"));
    }

    /**
     * After {@code ::}: {@code class}, {@code CONST}, {@code method()}, {@code $prop}, {@code $method()}, {@code {expr}}
     */
    private static IElementType parseStaticMember(PhpPsiBuilder builder) {
        if (builder.compareAndEat(kwCLASS)) {
            return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
        }
        if (builder.compare(VARIABLE)) {
            // static property: the VARIABLE token is a direct child of FIELD_REFERENCE
            builder.advanceLexer();
        }
        else if (builder.compare(DOLLAR)) {
            parseSimpleVariable(builder);
        }
        else if (builder.compareAndEat(LBRACE)) {
            parseRequired(builder);
            builder.match(RBRACE);
            if (builder.compare(LPAREN)) {
                parseArguments(builder);
                return PhpElementTypes.METHOD_REFERENCE;
            }
            return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
        }
        else if (PhpParserUtil.eatIdentifierLike(builder)) {
            if (builder.compare(LPAREN)) {
                parseArguments(builder);
                return PhpElementTypes.METHOD_REFERENCE;
            }
            return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
        }
        else {
            builder.error(PhpParserErrors.expected("member name"));
            return PhpElementTypes.CLASS_CONSTANT_REFERENCE;
        }

        if (builder.compare(LPAREN)) {
            parseArguments(builder);
            return PhpElementTypes.METHOD_REFERENCE;
        }
        return PhpElementTypes.FIELD_REFERENCE;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------
    // primary expressions

    private static IElementType parsePrimary(PhpPsiBuilder builder) {
        IElementType token = builder.getTokenType();
        if (token == null) {
            return PhpElementTypes.EMPTY_INPUT;
        }
        if (token != IDENTIFIER && PhpParserUtil.SEMI_RESERVED.contains(token) && PhpNameParser.canStartName(builder)) {
            return parseName(builder);
        }

        if (token == VARIABLE || token == DOLLAR) {
            return parseSimpleVariable(builder);
        }
        if (token == VARIABLE_NAME) {
            return done(builder, PhpElementTypes.VARIABLE_REFERENCE);
        }
        if (token == LPAREN) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            parseRequired(builder);
            builder.match(RPAREN);
            marker.done(PhpElementTypes.PARENTHESIZED_EXPRESSION);
            return PhpElementTypes.PARENTHESIZED_EXPRESSION;
        }
        if (token == LBRACKET) {
            return parseArrayLiteral(builder, RBRACKET);
        }
        if (token == kwARRAY && builder.lookAhead(1) == LPAREN) {
            return parseArrayLiteral(builder, RPAREN);
        }
        if (token == kwLIST) {
            return parseArrayLiteral(builder, RPAREN);
        }
        if (tsNUMBERS.contains(token)) {
            return done(builder, PhpElementTypes.NUMBER);
        }
        if (token == STRING_LITERAL || token == STRING_LITERAL_SINGLE_QUOTE) {
            String text = builder.getTokenText();
            IElementType result = done(builder, PhpElementTypes.STRING);
            if (token == STRING_LITERAL && text != null) {
                checkEscapes(builder, text);
            }
            return result;
        }
        if (token == chDOUBLE_QUOTE) {
            return parseInterpolated(builder, chDOUBLE_QUOTE, PhpElementTypes.STRING);
        }
        if (token == chBACKTRICK) {
            return parseInterpolated(builder, chBACKTRICK, PhpElementTypes.SHELL_COMMAND);
        }
        if (token == HEREDOC_START) {
            return parseInterpolated(builder, HEREDOC_END, PhpElementTypes.HEREDOC);
        }
        if (token == kwNEW) {
            return parseNew(builder);
        }
        if (token == kwFUNCTION || token == FN_KEYWORD || token == ATTRIBUTE_START) {
            return PhpDeclarationParser.parseClosure(builder);
        }
        if (token == STATIC_KEYWORD) {
            IElementType next = builder.lookAhead(1);
            if (next == kwFUNCTION || next == FN_KEYWORD) {
                return PhpDeclarationParser.parseClosure(builder);
            }
            PhpNameParser.parseClassReference(builder, true);
            return PhpElementTypes.CLASS_REFERENCE;
        }
        if (token == MATCH_KEYWORD) {
            return parseMatch(builder);
        }
        if (token == kwISSET) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            parseParenthesizedList(builder);
            marker.done(PhpElementTypes.ISSET_EXPRESSION);
            return PhpElementTypes.ISSET_EXPRESSION;
        }
        if (token == kwEMPTY || token == kwEVAL) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            builder.match(LPAREN);
            parseRequired(builder);
            builder.match(RPAREN);
            IElementType result = token == kwEMPTY ? PhpElementTypes.EMPTY_EXPRESSION : PhpElementTypes.EVAL_EXPRESSION;
            marker.done(result);
            return result;
        }
        if (token == kwEXIT) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            if (builder.compare(LPAREN)) {
                parseArguments(builder);
            }
            marker.done(PhpElementTypes.EXIT_EXPRESSION);
            return PhpElementTypes.EXIT_EXPRESSION;
        }
        if (PhpNameParser.canStartName(builder)) {
            return parseName(builder);
        }
        return PhpElementTypes.EMPTY_INPUT;
    }

    private static IElementType done(PhpPsiBuilder builder, IElementType elementType) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        marker.done(elementType);
        return elementType;
    }

    /**
     * {@code $a}, {@code $$a}, {@code ${expr}}
     */
    public static IElementType parseSimpleVariable(PhpPsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        if (builder.compareAndEat(VARIABLE)) {
            marker.done(PhpElementTypes.VARIABLE_REFERENCE);
            return PhpElementTypes.VARIABLE_REFERENCE;
        }
        builder.match(DOLLAR);
        if (builder.compareAndEat(LBRACE)) {
            parseRequired(builder);
            builder.match(RBRACE);
        }
        else if (builder.compare(VARIABLE) || builder.compare(DOLLAR)) {
            parseSimpleVariable(builder);
        }
        else {
            builder.error(PhpParserErrors.expected("variable"));
        }
        marker.done(PhpElementTypes.VARIABLE_REFERENCE);
        return PhpElementTypes.VARIABLE_REFERENCE;
    }

    /**
     * Name in expression: class reference before {@code ::}, function call before {@code (}, otherwise constant
     */
    private static IElementType parseName(PhpPsiBuilder builder) {
        PsiBuilder.Marker probe = builder.mark();
        boolean simpleName = builder.compare(IDENTIFIER);
        String text = builder.getTokenText();
        PhpNameParser.parseQualifiedName(builder);
        IElementType next = builder.getTokenType();
        boolean isDefine = simpleName && "define".equalsIgnoreCase(text) && next == LPAREN;
        probe.rollbackTo();

        if (next == SCOPE_RESOLUTION) {
            PhpNameParser.parseClassReference(builder, false);
            return PhpElementTypes.CLASS_REFERENCE;
        }

        PsiBuilder.Marker marker = builder.mark();
        PhpNameParser.parseQualifiedName(builder);
        if (next == LPAREN) {
            parseArguments(builder);
            marker.done(PhpElementTypes.FUNCTION_REFERENCE);
            if (isDefine) {
                marker.precede().done(PhpElementTypes.DEFINE);
                return PhpElementTypes.DEFINE;
            }
            return PhpElementTypes.FUNCTION_REFERENCE;
        }
        marker.done(PhpElementTypes.CONSTANT);
        return PhpElementTypes.CONSTANT;
    }

    private static IElementType parseInterpolated(PhpPsiBuilder builder, IElementType end, IElementType elementType) {
        PsiBuilder.Marker marker = builder.mark();
        String start = builder.getTokenText();
        builder.advanceLexer();
        int bodyStart = builder.getCurrentOffset();
        // nowdoc '<<<'X'' and backticks have no escape sequences to check
        parseEncapsList(builder, end != chBACKTRICK && (start == null || start.indexOf('\'') < 0));
        if (end == HEREDOC_END && builder.compare(HEREDOC_END)) {
            CharSequence body = builder.getOriginalText().subSequence(bodyStart, builder.getCurrentOffset());
            String closing = builder.getTokenText();
            builder.advanceLexer();
            checkHeredocIndentation(builder, body, closing == null ? "" : closing);
        }
        else {
            builder.match(end);
        }
        marker.done(elementType);
        return elementType;
    }

    /**
     * {@code \\u{...}}: one or more hex digits, at most U+10FFFF
     */
    private static void checkEscapes(PhpPsiBuilder builder, String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) != '\\') {
                continue;
            }
            char next = text.charAt(i + 1);
            if (next == 'u' && i + 2 < text.length() && text.charAt(i + 2) == '{') {
                int close = text.indexOf('}', i + 3);
                String digits = close < 0 ? "" : text.substring(i + 3, close);
                String significant = digits.replaceFirst("^0+", "");
                boolean valid = !digits.isEmpty() && digits.chars().allMatch(c -> Character.digit(c, 16) >= 0)
                    && significant.length() <= 6 && (significant.isEmpty() || Integer.parseInt(significant, 16) <= 0x10FFFF);
                if (!valid) {
                    builder.error("Invalid UTF-8 codepoint escape sequence");
                    return;
                }
            }
            // skip the escaped character
            i++;
        }
    }

    /**
     * Body lines must be indented at least like the closing label, with the same kind of whitespace
     */
    private static void checkHeredocIndentation(PhpPsiBuilder builder, CharSequence body, String closing) {
        String label = closing.startsWith("\r\n") ? closing.substring(2) : closing.startsWith("\n") || closing.startsWith("\r") ? closing.substring(1) : closing;
        int indentation = 0;
        while (indentation < label.length() && (label.charAt(indentation) == ' ' || label.charAt(indentation) == '\t')) {
            indentation++;
        }
        if (indentation == 0) {
            return;
        }
        String closingWhitespace = label.substring(0, indentation);
        if (closingWhitespace.indexOf(' ') >= 0 && closingWhitespace.indexOf('\t') >= 0) {
            builder.error("Invalid indentation - tabs and spaces cannot be mixed");
            return;
        }
        boolean spaces = closingWhitespace.charAt(0) == ' ';
        for (String line : body.toString().split("\r\n|\n|\r", -1)) {
            for (int i = 0; i < indentation && i < line.length(); i++) {
                char c = line.charAt(i);
                if (c != ' ' && c != '\t') {
                    builder.error("Invalid body indentation level (expecting an indentation level of at least " + indentation + ")");
                    return;
                }
                if ((c == ' ') != spaces) {
                    builder.error("Invalid indentation - tabs and spaces cannot be mixed");
                    return;
                }
            }
        }
    }

    private static final TokenSet ENCAPS_CONTENTS = TokenSet.create(HEREDOC_CONTENTS, STRING_LITERAL, EXEC_COMMAND, ESCAPE_SEQUENCE);

    /**
     * Contents of {@code "..."}, heredoc and backticks: text, {@code $var}, {@code $var[x]}, {@code $var->prop}, {@code {$expr}}, {@code ${name}}
     */
    private static void parseEncapsList(PhpPsiBuilder builder, boolean escapes) {
        while (true) {
            if (builder.compare(ENCAPS_CONTENTS)) {
                String text = builder.getTokenText();
                builder.advanceLexer();
                if (escapes && text != null) {
                    checkEscapes(builder, text);
                }
                continue;
            }
            if (builder.compare(VARIABLE)) {
                PsiBuilder.Marker marker = builder.mark();
                builder.advanceLexer();
                marker.done(PhpElementTypes.VARIABLE_REFERENCE);
                if (builder.compare(LBRACKET)) {
                    marker = marker.precede();
                    builder.advanceLexer();
                    PsiBuilder.Marker index = builder.mark();
                    builder.compareAndEat(opMINUS);
                    if (!builder.compareAndEat(TokenSet.create(IDENTIFIER, VARIABLE_OFFSET_NUMBER, VARIABLE, INTEGER_LITERAL))) {
                        builder.error(PhpParserErrors.expected("array index"));
                    }
                    index.done(PhpElementTypes.ARRAY_INDEX);
                    builder.match(RBRACKET);
                    marker.done(PhpElementTypes.ARRAY_ACCESS_EXPRESSION);
                }
                else if (builder.compare(MEMBER_ACCESS)) {
                    marker = marker.precede();
                    builder.advanceLexer();
                    builder.match(IDENTIFIER);
                    marker.done(PhpElementTypes.FIELD_REFERENCE);
                }
                continue;
            }
            if (builder.compareAndEat(LBRACE)) {
                // {$expr}
                parseRequired(builder);
                builder.match(RBRACE);
                continue;
            }
            if (builder.compare(DOLLAR_LBRACE)) {
                // ${name}, ${name[expr]}, ${expr}
                PsiBuilder.Marker marker = builder.mark();
                builder.advanceLexer();
                if (builder.compare(VARIABLE_NAME) && builder.lookAhead(1) == LBRACKET) {
                    builder.advanceLexer();
                    builder.advanceLexer();
                    PsiBuilder.Marker index = builder.mark();
                    parseRequired(builder);
                    index.done(PhpElementTypes.ARRAY_INDEX);
                    builder.match(RBRACKET);
                }
                else if (!builder.compareAndEat(VARIABLE_NAME)) {
                    parseRequired(builder);
                }
                builder.match(RBRACE);
                marker.done(PhpElementTypes.VARIABLE_REFERENCE);
                continue;
            }
            break;
        }
    }

    /**
     * {@code [a, k => v, ...$s, &$r]}, {@code array(...)} and {@code list(...)}; empty slots are allowed for destructuring
     */
    private static IElementType parseArrayLiteral(PhpPsiBuilder builder, IElementType end) {
        PsiBuilder.Marker marker = builder.mark();
        if (end == RPAREN) {
            // array( / list(
            builder.advanceLexer();
            builder.match(LPAREN);
        }
        else {
            builder.advanceLexer();
        }

        while (!builder.eof() && !builder.compare(end)) {
            if (builder.compareAndEat(opCOMMA)) {
                continue;
            }
            if (!parseArrayElement(builder)) {
                builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
                break;
            }
            if (!builder.compareAndEat(opCOMMA)) {
                break;
            }
        }
        builder.match(end);
        marker.done(PhpElementTypes.ARRAY_CREATION_EXPRESSION);
        return PhpElementTypes.ARRAY_CREATION_EXPRESSION;
    }

    private static boolean parseArrayElement(PhpPsiBuilder builder) {
        PsiBuilder.Marker element = builder.mark();
        PsiBuilder.Marker value = builder.mark();
        if (builder.compareAndEat(ELLIPSIS)) {
            parseRequired(builder);
            value.done(PhpElementTypes.ARRAY_VALUE);
            element.drop();
            return true;
        }
        if (builder.compare(opBIT_AND)) {
            builder.advanceLexer();
            parseRequired(builder);
            value.done(PhpElementTypes.ARRAY_VALUE);
            element.drop();
            return true;
        }

        if (parseDestructuringTarget(builder) == PhpElementTypes.EMPTY_INPUT) {
            value.drop();
            element.drop();
            return false;
        }
        if (!builder.compare(HASH_ARRAY)) {
            value.done(PhpElementTypes.ARRAY_VALUE);
            element.drop();
            return true;
        }

        value.done(PhpElementTypes.ARRAY_KEY);
        builder.advanceLexer();
        PsiBuilder.Marker hashValue = builder.mark();
        builder.compareAndEat(opBIT_AND);
        PhpParserUtil.expectExpression(builder, parseDestructuringTarget(builder));
        hashValue.done(PhpElementTypes.ARRAY_VALUE);
        element.done(PhpElementTypes.ARRAY_HASH);
        return true;
    }

    /**
     * Expression, or {@code list(...)} which is only valid as an assignment target
     */
    public static IElementType parseDestructuringTarget(PhpPsiBuilder builder) {
        if (!builder.compare(kwLIST)) {
            return parse(builder);
        }
        PsiBuilder.Marker assignment = builder.mark();
        parseArrayLiteral(builder, RPAREN);
        if (!builder.compareAndEat(opASGN)) {
            assignment.drop();
            return PhpElementTypes.ARRAY_CREATION_EXPRESSION;
        }
        parseRequired(builder, P_ASSIGN);
        assignment.done(PhpElementTypes.MULTIASSIGNMENT_EXPRESSION);
        return PhpElementTypes.MULTIASSIGNMENT_EXPRESSION;
    }

    /**
     * Expression statement and {@code for} init/step: the only places for the PHP 8.5 {@code (void)} cast
     */
    public static IElementType parseStatementExpression(PhpPsiBuilder builder) {
        if (!builder.compare(opVOID_CAST)) {
            return parse(builder);
        }
        PsiBuilder.Marker cast = builder.mark();
        builder.advanceLexer();
        parseRequired(builder);
        cast.done(PhpElementTypes.TYPE_CAST_EXPRESSION);
        return PhpElementTypes.TYPE_CAST_EXPRESSION;
    }

    /**
     * {@code isset(a, b,)}
     */
    private static void parseParenthesizedList(PhpPsiBuilder builder) {
        builder.match(LPAREN);
        while (!builder.eof() && !builder.compare(RPAREN)) {
            if (parse(builder) == PhpElementTypes.EMPTY_INPUT) {
                builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
                break;
            }
            if (!builder.compareAndEat(opCOMMA)) {
                break;
            }
        }
        builder.match(RPAREN);
    }

    /**
     * {@code (a, ...$b, name: c, &$d)} or the first-class callable syntax {@code (...)}
     */
    public static void parseArguments(PhpPsiBuilder builder) {
        builder.match(LPAREN);
        PsiBuilder.Marker list = builder.mark();
        if (builder.compare(ELLIPSIS) && builder.lookAhead(1) == RPAREN) {
            done(builder, PhpElementTypes.CALLABLE_PLACEHOLDER);
        }
        else {
            while (!builder.eof() && !builder.compare(RPAREN)) {
                if (!parseArgument(builder)) {
                    builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
                    break;
                }
                if (!builder.compareAndEat(opCOMMA)) {
                    break;
                }
            }
        }
        list.done(PhpElementTypes.EXPRESSION_PARAMETER_LIST);
        builder.match(RPAREN);
    }

    private static boolean parseArgument(PhpPsiBuilder builder) {
        // partial function application placeholders: 'f(?, 1, ...)'
        if (builder.compare(opQUEST) || builder.compare(ELLIPSIS)) {
            IElementType next = builder.lookAhead(1);
            if (next == opCOMMA || next == RPAREN) {
                done(builder, PhpElementTypes.CALLABLE_PLACEHOLDER);
                return true;
            }
        }
        if (builder.compare(ELLIPSIS)) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            parseRequired(builder);
            marker.done(PhpElementTypes.VARIADIC_ARGUMENT);
            return true;
        }
        if (PhpParserUtil.isIdentifierLike(builder) && builder.lookAhead(1) == opCOLON) {
            // a named argument is flat in the parameter list: the name, the colon and the value
            PhpParserUtil.eatIdentifierLike(builder);
            builder.advanceLexer();
            if (builder.compare(opQUEST) && (builder.lookAhead(1) == opCOMMA || builder.lookAhead(1) == RPAREN)) {
                done(builder, PhpElementTypes.CALLABLE_PLACEHOLDER);
            }
            else {
                parseRequired(builder);
            }
            return true;
        }
        if (builder.compareAndEat(opBIT_AND)) {
            // call-time pass-by-reference, removed in PHP 5.4
            parseRequired(builder);
            return true;
        }
        return parse(builder) != PhpElementTypes.EMPTY_INPUT;
    }

    /**
     * {@code new Foo}, {@code new Foo(args)}, {@code new $class}, {@code new (expr)}, {@code new class(args) extends A {...}}
     */
    private static IElementType parseNew(PhpPsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();

        PsiBuilder.Marker anonymousClass = builder.mark();
        PhpDeclarationParser.parseAttributes(builder);
        PhpDeclarationParser.parseClassModifiers(builder);
        if (builder.compareAndEat(kwCLASS)) {
            if (builder.compare(LPAREN)) {
                parseArguments(builder);
            }
            PhpDeclarationParser.parseClassTail(builder, kwCLASS);
            anonymousClass.done(PhpElementTypes.ANONYMOUS_CLASS);
            marker.done(PhpElementTypes.NEW_EXPRESSION);
            return PhpElementTypes.NEW_EXPRESSION;
        }
        anonymousClass.rollbackTo();

        if (builder.compare(STATIC_KEYWORD) || PhpNameParser.canStartName(builder)) {
            PsiBuilder.Marker staticProperty = builder.mark();
            PhpNameParser.parseClassReference(builder, true);
            if (builder.compare(SCOPE_RESOLUTION) && (builder.lookAhead(1) == VARIABLE || builder.lookAhead(1) == DOLLAR)) {
                // 'new A::$class'
                builder.advanceLexer();
                parseSimpleVariable(builder);
                staticProperty.done(PhpElementTypes.FIELD_REFERENCE);
            }
            else {
                staticProperty.drop();
            }
        }
        else if (builder.compare(VARIABLE) || builder.compare(DOLLAR)) {
            parseDynamicClassName(builder);
        }
        else if (builder.compareAndEat(LPAREN)) {
            parseRequired(builder);
            builder.match(RPAREN);
        }
        else {
            builder.error(PhpParserErrors.expected("class name"));
        }

        boolean hasArguments = builder.compare(LPAREN);
        if (hasArguments) {
            parseArguments(builder);
        }
        marker.done(PhpElementTypes.NEW_EXPRESSION);
        if (!hasArguments && builder.compare(DEREFERENCE)) {
            // PHP 8.4 'new Foo()->bar()' requires the argument list
            builder.error(PhpParserErrors.expected(LPAREN));
        }
        return PhpElementTypes.NEW_EXPRESSION;
    }

    /**
     * {@code new $a->b::$c['d']} - variable with property and static property fetches, no calls
     */
    private static void parseDynamicClassName(PhpPsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        IElementType result = parseSimpleVariable(builder);
        while (true) {
            if (builder.compare(MEMBER_ACCESS)) {
                builder.advanceLexer();
                parseMemberName(builder);
                result = PhpElementTypes.FIELD_REFERENCE;
            }
            else if (builder.compare(SCOPE_RESOLUTION) && builder.lookAhead(1) == VARIABLE) {
                builder.advanceLexer();
                builder.advanceLexer();
                result = PhpElementTypes.FIELD_REFERENCE;
            }
            else if (builder.compareAndEat(LBRACKET)) {
                PsiBuilder.Marker index = builder.mark();
                parseRequired(builder);
                index.done(PhpElementTypes.ARRAY_INDEX);
                builder.match(RBRACKET);
                result = PhpElementTypes.ARRAY_ACCESS_EXPRESSION;
            }
            else {
                break;
            }
            marker.done(result);
            marker = marker.precede();
        }
        marker.drop();
    }

    /**
     * {@code match (expr) { a, b => x, default => y, }}
     */
    private static IElementType parseMatch(PhpPsiBuilder builder) {
        PsiBuilder.Marker marker = builder.mark();
        builder.advanceLexer();
        builder.match(LPAREN);
        parseRequired(builder);
        builder.match(RPAREN);
        builder.match(LBRACE);
        while (!builder.eof() && !builder.compare(RBRACE)) {
            PsiBuilder.Marker arm = builder.mark();
            if (builder.compareAndEat(kwDEFAULT)) {
                builder.compareAndEat(opCOMMA);
            }
            else {
                do {
                    if (builder.compare(HASH_ARRAY)) {
                        break;
                    }
                    if (parse(builder) == PhpElementTypes.EMPTY_INPUT) {
                        builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
                        break;
                    }
                }
                while (builder.compareAndEat(opCOMMA));
            }
            builder.match(HASH_ARRAY);
            parseRequired(builder);
            arm.done(PhpElementTypes.MATCH_ARM);
            if (!builder.compareAndEat(opCOMMA)) {
                break;
            }
        }
        builder.match(RBRACE);
        marker.done(PhpElementTypes.MATCH_EXPRESSION);
        return PhpElementTypes.MATCH_EXPRESSION;
    }
}
