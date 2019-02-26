package consulo.php.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import consulo.php.lang.highlighter.PhpSyntaxHighlighter;

/**
 * @author VISTALL
 * @since 2019-02-26
 */
public class PhpEditorHighlighterProvider implements EditorHighlighterProvider
{
	@Override
	public EditorHighlighter getEditorHighlighter(@Nullable Project project, @Nonnull FileType fileType, @Nullable VirtualFile virtualFile, @Nonnull EditorColorsScheme colors)
	{
		return new PhpSyntaxHighlighter(project, virtualFile, colors);
	}
}
