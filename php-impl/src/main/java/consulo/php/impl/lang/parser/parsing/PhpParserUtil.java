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
import consulo.php.impl.lang.lexer.PhpKeywords;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.php.impl.lang.parser.util.PhpParserErrors;
import consulo.php.impl.lang.parser.util.PhpPsiBuilder;
import consulo.php.lang.lexer.PhpTokenTypes;

/**
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpParserUtil implements PhpTokenTypes {
    /**
     * PHP 7+ "semi-reserved" words: keywords that are still valid as member, constant and label names
     */
    public static final TokenSet SEMI_RESERVED = TokenSet.orSet(PhpKeywords.types(), TokenSet.create(READONLY_KEYWORD, ENUM_KEYWORD));

    public static final TokenSet MEMBER_MODIFIERS = TokenSet.create(PUBLIC_KEYWORD, PROTECTED_KEYWORD, PRIVATE_KEYWORD, STATIC_KEYWORD, ABSTRACT_KEYWORD,
        FINAL_KEYWORD, READONLY_KEYWORD, kwVAR);

    public static final TokenSet VISIBILITY = TokenSet.create(PUBLIC_KEYWORD, PROTECTED_KEYWORD, PRIVATE_KEYWORD);

    private PhpParserUtil() {
    }

    public static boolean isIdentifierLike(PhpPsiBuilder builder) {
        return builder.compare(IDENTIFIER) || builder.compare(SEMI_RESERVED);
    }

    public static boolean isIdentifierLike(IElementType type) {
        return type == IDENTIFIER || SEMI_RESERVED.contains(type);
    }

    /**
     * Eats an identifier or a keyword used as a name, the keyword is remapped to {@link #IDENTIFIER}
     */
    public static boolean eatIdentifierLike(PhpPsiBuilder builder) {
        if (builder.compare(SEMI_RESERVED)) {
            builder.remapCurrentToken(IDENTIFIER);
        }
        return builder.compareAndEat(IDENTIFIER);
    }

    /**
     * Next raw token (whitespace and comments included) is the given type: names and {@code private(set)} must not contain whitespace
     */
    public static boolean isRawNext(PhpPsiBuilder builder, IElementType type) {
        return builder.rawLookup(1) == type;
    }

    public static boolean isContextual(PhpPsiBuilder builder, String word) {
        return builder.compare(IDENTIFIER) && word.equalsIgnoreCase(builder.getTokenText());
    }

    /**
     * {@code readonly} is a keyword, but stays an identifier when used as a function name
     */
    public static boolean remapReadonly(PhpPsiBuilder builder) {
        return remapReadonly(builder, false);
    }

    /**
     * @param always in member context {@code readonly (A&B)|C $x} is a modifier followed by a DNF type
     */
    public static boolean remapReadonly(PhpPsiBuilder builder, boolean always) {
        if (isContextual(builder, "readonly") && (always || builder.lookAhead(1) != LPAREN)) {
            builder.remapCurrentToken(READONLY_KEYWORD);
            return true;
        }
        return builder.compare(READONLY_KEYWORD);
    }

    /**
     * Statement terminator: ';' or a closing tag (which is consumed by the statement list)
     */
    public static void expectStatementEnd(PhpPsiBuilder builder) {
        if (!builder.compare(PHP_CLOSING_TAG)) {
            builder.match(opSEMICOLON);
        }
    }

    public static void expectExpression(PhpPsiBuilder builder, IElementType result) {
        if (result == PhpElementTypes.EMPTY_INPUT) {
            builder.error(PhpParserErrors.EXPRESSION_EXPECTED_MESSAGE);
        }
    }
}
