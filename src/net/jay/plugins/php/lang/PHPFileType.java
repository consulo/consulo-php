package net.jay.plugins.php.lang;

import javax.swing.Icon;

import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons2;
import net.jay.plugins.php.lang.highlighter.PHPSyntaxHighlighter;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PHPFileType extends LanguageFileType
{
	public static final PHPFileType INSTANCE = new PHPFileType();

	public static final String DEFAULT_EXTENSION = "php";
	public static final String DESCRIPTION = PHPBundle.message("filetype.description");
	public static final String NAME = "PHP";
	public static final
	@NonNls
	String[] EXTENTIONS = new String[]{
			DEFAULT_EXTENSION,
			"inc",
			"phtml",
			"php3"
	};

	protected PHPFileType()
	{
		super(PHPLanguage.INSTANCE);
		FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, new EditorHighlighterProvider()
		{
			@Override
			public EditorHighlighter getEditorHighlighter(@Nullable Project project, @NotNull FileType fileType, @Nullable VirtualFile virtualFile, @NotNull EditorColorsScheme editorColorsScheme)
			{
				return new PHPSyntaxHighlighter(project, virtualFile, editorColorsScheme);
			}
		});
	}

	@NotNull
	@NonNls
	public String getName()
	{
		return NAME;
	}

	@NotNull
	public String getDescription()
	{
		return DESCRIPTION;
	}

	@NotNull
	@NonNls
	public String getDefaultExtension()
	{
		return DEFAULT_EXTENSION;
	}

	@Nullable
	public Icon getIcon()
	{
		return PHPIcons2.Php;
	}

}
