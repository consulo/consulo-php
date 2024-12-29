package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.EditorHighlighter;
import consulo.colorScheme.EditorColorsScheme;
import consulo.language.editor.highlight.EditorHighlighterProvider;
import consulo.php.impl.lang.highlighter.PhpSyntaxHighlighter;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-02-26
 */
@ExtensionImpl
public class PhpEditorHighlighterProvider implements EditorHighlighterProvider
{
	@Override
	public EditorHighlighter getEditorHighlighter(@Nullable Project project, @Nonnull FileType fileType, @Nullable VirtualFile virtualFile, @Nonnull EditorColorsScheme colors)
	{
		return new PhpSyntaxHighlighter(project, virtualFile, colors);
	}

	@Nonnull
	@Override
	public FileType getFileType()
	{
		return PhpFileType.INSTANCE;
	}
}
