package consulo.php.impl.lang.highlighter;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.colorScheme.EditorColorsScheme;
import consulo.language.editor.highlight.LayerDescriptor;
import consulo.language.editor.highlight.LayeredLexerEditorHighlighter;
import consulo.language.editor.highlight.SyntaxHighlighterFactory;
import consulo.language.version.LanguageVersionResolver;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.xml.ide.highlighter.HtmlFileType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PhpSyntaxHighlighter extends LayeredLexerEditorHighlighter
{
	public PhpSyntaxHighlighter(@Nullable final Project project, @Nullable final VirtualFile virtualFile, @Nonnull final EditorColorsScheme colors)
	{
		super(new PhpFileSyntaxHighlighter(LanguageVersionResolver.forLanguage(PhpLanguage.INSTANCE).getLanguageVersion(PhpLanguage
				.INSTANCE, project, virtualFile)), colors);
		registerLayer(PhpTokenTypes.HTML, new LayerDescriptor(SyntaxHighlighterFactory.getSyntaxHighlighter(HtmlFileType.INSTANCE, project,
				virtualFile), ""));
	}
}
