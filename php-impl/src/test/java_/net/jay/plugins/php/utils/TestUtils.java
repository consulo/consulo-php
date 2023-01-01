package net.jay.plugins.php.utils;

import consulo.language.file.FileTypeManager;
import consulo.project.Project;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.util.IncorrectOperationException;
import consulo.util.lang.LocalTimeCounter;
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
     * @throws IncorrectOperationException
     *
     */
    public static PsiFile createPseudoPhysicalFile(final Project project, final String text) throws IncorrectOperationException
	{
        return PsiFileFactory.getInstance(project).createFileFromText(TEMP_FILE, FileTypeManager.getInstance().getFileTypeByFileName(TEMP_FILE),
                text, LocalTimeCounter.currentTime(), true);
	}
}
