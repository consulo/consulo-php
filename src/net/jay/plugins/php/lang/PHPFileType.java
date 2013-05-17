package net.jay.plugins.php.lang;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.lang.highlighter.PHPSyntaxHighlighter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 22.02.2007
 *
 * @author jay
 */
public class PHPFileType extends LanguageFileType {
    public static final PHPFileType PHP = new PHPFileType();
    public static final String DEFAULT_EXTENSION = "php";
    public static final String DESCRIPTION = PHPBundle.message("filetype.description");
    public static final String NAME = "PHP";
    public static final
    @NonNls
    String[] EXTENTIONS = new String[]{DEFAULT_EXTENSION, "inc", "phtml", "php3"};

    protected PHPFileType() {
        super(new PHPLanguage());
    }

    @NotNull
    @NonNls
    public String getName() {
        return NAME;
    }

    @NotNull
    public String getDescription() {
        return DESCRIPTION;
    }

    @NotNull
    @NonNls
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    public Icon getIcon() {
        return PHPIcons.PHP_ICON;
    }

    @Override
    public EditorHighlighter getEditorHighlighter(@Nullable final Project project, @Nullable final VirtualFile virtualFile, @NotNull EditorColorsScheme colors) {
        return new PHPSyntaxHighlighter(project, virtualFile, colors);
    }
}
