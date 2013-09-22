package org.consulo.php.lang.highlighter;

import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.LanguageVersionResolvers;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PhpSyntaxHighlighter extends LayeredLexerEditorHighlighter
{
	public PhpSyntaxHighlighter(@Nullable final Project project, @Nullable final VirtualFile virtualFile, @NotNull final EditorColorsScheme colors)
	{
		super(new PhpFileSyntaxHighlighter(LanguageVersionResolvers.INSTANCE.forLanguage(PhpLanguage.INSTANCE).getLanguageVersion(PhpLanguage.INSTANCE, project, virtualFile)), colors);
		registerLayer(PhpTokenTypes.HTML, new LayerDescriptor(SyntaxHighlighter.PROVIDER.create(StdFileTypes.HTML, project, virtualFile), ""));
	}
}
