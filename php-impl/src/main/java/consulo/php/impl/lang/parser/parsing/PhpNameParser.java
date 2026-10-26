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
import org.jspecify.annotations.Nullable;

/**
 * Names and types.
 * <p>
 * A class name {@code \A\B\C} becomes nested references: {@code CLASS_REFERENCE(CLASS_REFERENCE(CLASS_REFERENCE(\ A) \ B) \ C)},
 * a function or constant name keeps the namespace part in {@code NAMESPACE_REFERENCE} and the short name as a direct {@code IDENTIFIER}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpNameParser implements PhpTokenTypes {
    private static final TokenSet TYPE_KEYWORDS = TokenSet.create(kwARRAY, STATIC_KEYWORD);

    private PhpNameParser() {
    }

    public static boolean canStartName(PhpPsiBuilder builder) {
        if (builder.compare(IDENTIFIER)) {
            return true;
        }
        if (builder.compare(SLASH)) {
            return PhpParserUtil.isIdentifierLike(builder.rawLookup(1));
        }
        if (builder.compare(NAMESPACE_KEYWORD)) {
            return PhpParserUtil.isRawNext(builder, SLASH);
        }
        // 'List\Foo' - reserved words are allowed in qualified names
        return builder.compare(PhpParserUtil.SEMI_RESERVED) && PhpParserUtil.isRawNext(builder, SLASH) && PhpParserUtil.isIdentifierLike(builder.rawLookup(2));
    }

    /**
     * {@code \Segment} directly continuing a name
     */
    private static boolean isNameContinuation(PhpPsiBuilder builder) {
        return builder.compare(SLASH) && !isWhitespaceOrComment(builder.rawLookup(-1)) && PhpParserUtil.isIdentifierLike(builder.rawLookup(1));
    }

    private static boolean isWhitespaceOrComment(@Nullable IElementType type) {
        return type == WHITE_SPACE || type == LINE_COMMENT || type == C_STYLE_COMMENT || type == PhpDocElementTypes.DOC_COMMENT;
    }

    private static void eatSegment(PhpPsiBuilder builder) {
        if (!PhpParserUtil.eatIdentifierLike(builder)) {
            builder.error(PhpParserErrors.expected(IDENTIFIER));
        }
    }

    /**
     * @param allowTypeKeywords accept {@code static} and {@code array} (types, {@code new static}, {@code static::})
     */
    public static PsiBuilder.@Nullable Marker parseClassReference(PhpPsiBuilder builder, boolean allowTypeKeywords) {
        if (allowTypeKeywords && builder.compare(TYPE_KEYWORDS)) {
            PsiBuilder.Marker marker = builder.mark();
            builder.advanceLexer();
            marker.done(PhpElementTypes.CLASS_REFERENCE);
            return marker;
        }

        if (!canStartName(builder)) {
            return null;
        }

        PsiBuilder.Marker marker = builder.mark();
        if (builder.compareAndEat(NAMESPACE_KEYWORD)) {
            builder.match(SLASH);
        }
        else {
            builder.compareAndEat(SLASH);
        }
        eatSegment(builder);
        marker.done(PhpElementTypes.CLASS_REFERENCE);

        while (isNameContinuation(builder)) {
            marker = marker.precede();
            builder.advanceLexer();
            eatSegment(builder);
            marker.done(PhpElementTypes.CLASS_REFERENCE);
        }
        return marker;
    }

    /**
     * Function or constant name: namespace part (if any) as {@code NAMESPACE_REFERENCE}, then the short name
     */
    public static boolean parseQualifiedName(PhpPsiBuilder builder) {
        if (!canStartName(builder)) {
            return false;
        }

        PsiBuilder.Marker namespace = builder.mark();
        boolean qualified = false;
        if (builder.compare(NAMESPACE_KEYWORD) || builder.compare(SLASH)) {
            if (builder.compareAndEat(NAMESPACE_KEYWORD)) {
                qualified = true;
            }
            if (builder.compareAndEat(SLASH)) {
                qualified = true;
            }
        }
        while (PhpParserUtil.isIdentifierLike(builder) && PhpParserUtil.isRawNext(builder, SLASH)
            && PhpParserUtil.isIdentifierLike(builder.rawLookup(2))) {
            eatSegment(builder);
            builder.advanceLexer();
            qualified = true;
        }
        if (qualified) {
            namespace.done(PhpElementTypes.NAMESPACE_REFERENCE);
            eatSegment(builder);
        }
        else {
            namespace.drop();
            builder.match(IDENTIFIER);
        }
        return true;
    }

    public static boolean canStartType(PhpPsiBuilder builder) {
        return builder.compare(opQUEST) || builder.compare(LPAREN) || builder.compare(TYPE_KEYWORDS) || canStartName(builder);
    }

    /**
     * {@code ?T}, {@code A|B}, {@code A&B}, {@code (A&B)|null}
     */
    public static boolean parseType(PhpPsiBuilder builder) {
        if (builder.compareAndEat(opQUEST)) {
            if (parseClassReference(builder, true) == null) {
                builder.error("Type expected");
            }
            return true;
        }

        if (!parseUnionPart(builder)) {
            return false;
        }
        while (builder.compare(opBIT_OR)) {
            builder.advanceLexer();
            if (!parseUnionPart(builder)) {
                builder.error("Type expected");
                break;
            }
        }
        return true;
    }

    private static boolean parseUnionPart(PhpPsiBuilder builder) {
        if (builder.compareAndEat(LPAREN)) {
            parseIntersection(builder);
            builder.match(RPAREN);
            return true;
        }
        if (!canStartName(builder) && !builder.compare(TYPE_KEYWORDS)) {
            return false;
        }
        parseIntersection(builder);
        return true;
    }

    private static void parseIntersection(PhpPsiBuilder builder) {
        if (parseClassReference(builder, true) == null) {
            builder.error("Type expected");
            return;
        }
        // 'A & $x' and 'A &...$x' are by-reference parameters, not intersections
        while (builder.compare(opBIT_AND) && builder.lookAhead(1) != VARIABLE && builder.lookAhead(1) != ELLIPSIS) {
            builder.advanceLexer();
            if (parseClassReference(builder, true) == null) {
                builder.error("Type expected");
                return;
            }
        }
    }

    /**
     * A type wrapped into its own node, see {@link PhpElementTypes#TYPE_DECLARATION} and {@link PhpElementTypes#FIELD_TYPE}
     */
    public static boolean parseTypeDeclaration(PhpPsiBuilder builder, IElementType elementType) {
        PsiBuilder.Marker declaration = builder.mark();
        if (parseType(builder)) {
            declaration.done(elementType);
            return true;
        }
        declaration.drop();
        return false;
    }

    /**
     * {@code ': type'} of functions, methods and closures
     */
    public static void parseReturnTypeIfColon(PhpPsiBuilder builder) {
        if (!builder.compareAndEat(opCOLON)) {
            return;
        }
        PsiBuilder.Marker returnType = builder.mark();
        if (parseType(builder)) {
            returnType.done(PhpElementTypes.RETURN_TYPE);
        }
        else {
            returnType.drop();
            builder.error("Return type expected");
        }
    }
}
