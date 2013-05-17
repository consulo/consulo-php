package net.jay.plugins.php.lang.parser.util;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import net.jay.plugins.php.PHPBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author markov
 * @date 14.10.2007
 */
public class PHPParserErrors {

	private static final String EXPECTED_MESSAGE = PHPBundle.message("parsing.message.expected");
	private static final String UNEXPECTED_MESSAGE = PHPBundle.message("parsing.message.unexpected");
	public static final String EXPRESSION_EXPECTED_MESSAGE = expected(PHPBundle.message("parsing.error.expr.expr_expected"));

	private static HashMap<TokenSet, String> errorCache = new HashMap<TokenSet, String>();

	@NotNull
	public static String expected(String s) {
		return EXPECTED_MESSAGE + " " + s;
	}

	@NotNull
	private static String unexpected(String s) {
		return UNEXPECTED_MESSAGE + " " + s;
	}

	@NotNull
	public static String expected(IElementType type) {
		return expected(getPresentableName(type));
	}

	@Nullable
	private static String getPresentableName(final IElementType type) {
		return type.toString();
	}

	@NotNull
	public static String unexpected(IElementType type) {
		return unexpected(getPresentableName(type));
	}

	@NotNull
	public static String expected(TokenSet types) {
		return EXPECTED_MESSAGE + " " + setToString(types);
	}

	@NotNull
	public static String unexpected(TokenSet types) {
		return UNEXPECTED_MESSAGE + " " + setToString(types);
	}

	@NotNull
	private static String setToString(@NotNull final TokenSet set) {
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
