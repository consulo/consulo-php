package consulo.php.impl.lang.lexer;

import consulo.language.lexer.FlexAdapter;
import consulo.php.PhpLanguageLevel;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public class PhpFlexLexer extends FlexAdapter
{
	public PhpFlexLexer(boolean highlightingMode, PhpLanguageLevel languageLevel)
	{
		super(new _PhpFlexLexer(highlightingMode, languageLevel));
	}
}
