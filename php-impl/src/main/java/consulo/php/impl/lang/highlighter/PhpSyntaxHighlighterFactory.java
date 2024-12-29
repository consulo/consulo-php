package consulo.php.impl.lang.highlighter;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.LanguageVersionableSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;
import consulo.language.version.LanguageVersion;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
@ExtensionImpl
public class PhpSyntaxHighlighterFactory extends LanguageVersionableSyntaxHighlighterFactory
{
	@Nonnull
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(@Nonnull LanguageVersion languageVersion)
	{
		return new PhpFileSyntaxHighlighter(languageVersion);
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
