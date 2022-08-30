package consulo.php.impl.lang.braceMatcher;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.Language;
import consulo.language.PairedBraceMatcher;
import consulo.php.lang.lexer.PhpTokenTypes;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @time 21.12.2007 18:36:34
 */
@ExtensionImpl
public class PhpBraceMatcher implements PairedBraceMatcher, PhpTokenTypes
{
	private static final BracePair[] ourPairs = new BracePair[]{
			new BracePair(LBRACE, RBRACE, true),
			new BracePair(PHP_OPENING_TAG, PHP_CLOSING_TAG, false),
			new BracePair(PHP_ECHO_OPENING_TAG, PHP_CLOSING_TAG, false),
			new BracePair(LBRACKET, RBRACKET, false),
			new BracePair(LPAREN, RPAREN, false)
	};

	@Override
	public BracePair[] getPairs()
	{
		return ourPairs;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
