package net.jay.plugins.php.utils;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.LocalTimeCounter;
import org.jetbrains.annotations.NonNls;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 27.02.2007
 *
 * @author jay
 */
public class TestUtils {

    @NonNls
    private final static String TEMP_FILE = "temp.php";

    /**
     * Creates pseudophysical file by given name and content
     *
     * @param project Current project
     * @param text    Content for file to be created
     * @return PsiFile - the resulting file
     * @throws com.intellij.util.IncorrectOperationException
     *
     */
    public static PsiFile createPseudoPhysicalFile(final Project project, final String text) throws IncorrectOperationException {
        return PsiFileFactory.getInstance(project).createFileFromText(TEMP_FILE, FileTypeManager.getInstance().getFileTypeByFileName(TEMP_FILE),
                text, LocalTimeCounter.currentTime(), true);
	}
}
