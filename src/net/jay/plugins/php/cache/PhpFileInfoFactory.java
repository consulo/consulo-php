package net.jay.plugins.php.cache;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.util.VirtualFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date May 28, 2008 11:39:40 PM
 */
public class  PhpFileInfoFactory {

  /**
   * Creates new PhpFileInfo for given file
   *
   * @param project Current project
   * @param file    current file
   * @return PhpFileInfo object containing information about file
   *         or null if file cannot be found or isn't php file
   */
  @Nullable
  public static PhpFileInfo createFileInfo(@NotNull final Project project, @NotNull final VirtualFile file) {
    final PHPFile phpFile = getPHPFile(project, file);
    if (phpFile == null) {
      return null;
    }

    return createFileInfo(file, phpFile);
  }

  /**
   * Gets PsiFile by given virtualFile
   *
   * @param project Current project
   * @param file    VirtualFile
   * @return PHPFile object if found, null otherwise
   */
  private static PHPFile getPHPFile(@NotNull final Project project, @NotNull final VirtualFile file) {
    if (!file.isValid()) {
      return null;
    }
    final PsiManager myPsiManager = PsiManager.getInstance(project);
    final PsiFile psiFile = myPsiManager.findFile(file);
    if (psiFile == null || !(psiFile instanceof PHPFile)) {
      return null;
    }
    return (PHPFile) psiFile;
  }

  /**
   * Creates PhpFileInfo by PHPFile
   *
   * @param file  VirtualFile
   * @param phpFile PHPFile
   * @return PhpFileInfo object, containing information about PHPFile inside
   */
  private static PhpFileInfo createFileInfo(@NotNull final VirtualFile file, @NotNull final PHPFile phpFile) {
    final PhpFileInfo fileInfo = new PhpFileInfo(file.getUrl(), file.getTimeStamp(), phpFile.getProject());
    fileInfo.setLightPhpFile(phpFile.getLightCopy(fileInfo));
    return fileInfo;
  }

  /**
   * Creates PhpFileInfo by pseudophysical PHPFile. (for tests only)
   *
   * @param phpFile PHPFile
   * @return PhpFileInfo object, containing information about PHPFile inside
   */
  public static PhpFileInfo createFileInfoByPseudPhysicalFile(@NotNull final PHPFile phpFile) {
    final PhpFileInfo fileInfo = new PhpFileInfo(VirtualFileUtil.constructLocalUrl(phpFile.getName()), 0, phpFile.getProject());
    fileInfo.setLightPhpFile(phpFile.getLightCopy(fileInfo));
    return fileInfo;
  }
}
