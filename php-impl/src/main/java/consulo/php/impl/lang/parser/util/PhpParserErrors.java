package consulo.php.impl.lang.parser.util;

import consulo.language.ast.IElementType;
import consulo.language.ast.TokenSet;
import consulo.localize.LocalizeValue;
import consulo.php.localize.PhpLocalize;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author markov
 * @since 14.10.2007
 */
public class PhpParserErrors {
    public static final LocalizeValue EXPRESSION_EXPECTED_MESSAGE = expected(PhpLocalize.parsingErrorExprExpr_expected());

    private static HashMap<TokenSet, String> errorCache = new HashMap<TokenSet, String>();

    @Nonnull
    public static LocalizeValue expected(LocalizeValue value) {
        return LocalizeValue.join(PhpLocalize.parsingMessageExpected(), LocalizeValue.space(), value);
    }

    @Nonnull
    public static LocalizeValue expected(String s) {
        return LocalizeValue.join(PhpLocalize.parsingMessageExpected(), LocalizeValue.space(), LocalizeValue.of(s));
    }

    @Nonnull
    private static LocalizeValue unexpected(String s) {
        return LocalizeValue.join(PhpLocalize.parsingMessageUnexpected(), LocalizeValue.space(), LocalizeValue.of(s));
    }

    @Nonnull
    public static LocalizeValue expected(IElementType type) {
        return expected(getPresentableName(type));
    }

    @Nullable
    private static String getPresentableName(final IElementType type) {
        return type.toString();
    }

    @Nonnull
    public static LocalizeValue unexpected(IElementType type) {
        return unexpected(getPresentableName(type));
    }

    @Nonnull
    public static LocalizeValue expected(TokenSet types) {
        return LocalizeValue.join(PhpLocalize.parsingMessageExpected(), LocalizeValue.space(), LocalizeValue.of(setToString(types)));
    }

    @Nonnull
    public static LocalizeValue unexpected(TokenSet types) {
        return LocalizeValue.join(PhpLocalize.parsingMessageUnexpected(), LocalizeValue.space(), LocalizeValue.of(setToString(types)));
    }

    @Nonnull
    private static String setToString(@Nonnull final TokenSet set) {
        final String cachedString = errorCache.get(set);
        if (cachedString != null) {
            return cachedString;
        }

        // stringSet used for not to add tokens with similar text, tCOLON2 and tCOLON3 for example!
        final HashSet<String> stringSet = new HashSet<String>();
        final StringBuilder buffer = new StringBuilder();
        for (IElementType myToken : set.getTypes()) {
            final String tokenText = getPresentableName(myToken);
            if (tokenText == null || tokenText.length() == 0) {
                continue;
            }
            if (!stringSet.contains(tokenText)) {
                if (buffer.length() != 0) {
                    buffer.append(" or ");
                }
                stringSet.add(tokenText);
                buffer.append(tokenText);
            }
        }
        final String errorString = buffer.toString();
        errorCache.put(set, errorString);
        return errorString;
    }

}
