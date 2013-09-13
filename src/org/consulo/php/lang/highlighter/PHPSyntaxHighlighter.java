package org.consulo.php.lang.highlighter;

import com.intellij.lang.LanguageVersionResolvers;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.consulo.php.lang.PHPLanguage;
import org.consulo.php.lang.lexer.PHPTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PHPSyntaxHighlighter extends LayeredLexerEditorHighlighter
{
	public PHPSyntaxHighlighter(@Nullable final Project project, @Nullable final VirtualFile virtualFile, @NotNull final EditorColorsScheme colors)
	{
		super(new PhpFileSyntaxHighlighter(LanguageVersionResolvers.INSTANCE.forLanguage(PHPLanguage.INSTANCE).getLanguageVersion(PHPLanguage.INSTANCE, project, virtualFile)), colors);
		registerLayer(PHPTokenTypes.HTML, new LayerDescriptor(SyntaxHighlighter.PROVIDER.create(StdFileTypes.HTML, project, virtualFile), ""));
	}
}
