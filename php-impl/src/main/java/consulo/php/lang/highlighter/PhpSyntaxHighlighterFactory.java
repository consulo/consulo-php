package consulo.php.lang.highlighter;

import consulo.php.lang.PhpLanguage;
import org.jetbrains.annotations.NotNull;
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

	@NotNull
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(@NotNull LanguageVersion languageVersion)
	{
		return new PhpFileSyntaxHighlighter(languageVersion);
	}
}
