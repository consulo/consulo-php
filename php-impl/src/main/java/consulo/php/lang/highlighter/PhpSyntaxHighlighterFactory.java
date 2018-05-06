package consulo.php.lang.highlighter;

import javax.annotation.Nonnull;

import consulo.php.lang.PhpLanguage;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import consulo.fileTypes.LanguageVersionableSyntaxHighlighterFactory;
import consulo.lang.LanguageVersion;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpSyntaxHighlighterFactory extends LanguageVersionableSyntaxHighlighterFactory
{
	public PhpSyntaxHighlighterFactory()
	{
		super(PhpLanguage.INSTANCE);
	}

	@Nonnull
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(@Nonnull LanguageVersion languageVersion)
	{
		return new PhpFileSyntaxHighlighter(languageVersion);
	}
}
