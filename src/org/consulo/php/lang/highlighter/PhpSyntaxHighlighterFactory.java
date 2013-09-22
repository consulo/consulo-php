package org.consulo.php.lang.highlighter;

import org.consulo.fileTypes.LanguageVersionableSyntaxHighlighterFactory;
import org.consulo.php.lang.PhpLanguage;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.LanguageVersion;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;

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
