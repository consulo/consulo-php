package net.jay.plugins.php.util;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleFileIndex;
import com.intellij.openapi.roots.ContentIterator;
import net.jay.plugins.php.lang.PHPFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author jay
 * @date May 29, 2008 12:45:41 PM
 */
public class PhpVirtualFileScanner {

  /**
   * Searches all php files in directory.
   * <p/>
   * Use this method for files that doesn't belong to any module, othwerwise
   * see <code>searchRubyFilesUnderDirectory(@Nullable final Module module, @NotNull final VirtualFile dir)</code>
   *
   * @param file     File or directory
   * @param allFiles Method adds all found files into this set
   */
  public static void addFiles(@Nullable final VirtualFile file,
                              @NotNull final Set<VirtualFile> allFiles) {
    if (file == null
      || FileTypeManager.getInstance().isFileIgnored(file.getName())) {
      return;
    }
    if (!file.isDirectory()) {
      if (isPhpFile(file)) {
        allFiles.add(file);
      }
      return;
    }
    final VirtualFile[] children = file.getChildren();
    for (final VirtualFile child : children) {
      addFiles(child, allFiles);
    }
  }

  public static boolean isPhpFile(@Nullable final VirtualFile fileOrDir) {
    return !(fileOrDir == null || fileOrDir.isDirectory()) &&
      isPhpFile(fileOrDir.getName());
  }

  /**
   * @param fileName file name with extension
   * @return if file with such name and extension may be php file
   */
  public static boolean isPhpFile(@NotNull final String fileName) {
    final FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(fileName);
    return fileType instanceof PHPFileType;
  }

  /**
   * Finds all the relative paths
   *
   * @param rootDirectory root File
   * @return List of relative pathes to php files found in rootFile
   */
  public static List<String> getRelativeUrls(@NotNull final VirtualFile rootDirectory) {
    final List<String> relativeUrls = new ArrayList<String>();
    return addRelativeUrls(rootDirectory, relativeUrls);
  }

  public static List<String> addRelativeUrls(@NotNull final VirtualFile rootDirectory,
                                             @NotNull final List<String> relativeUrls) {
    final String rootUrl = rootDirectory.getUrl() + '/';
    final int length = rootUrl.length();
    Set<VirtualFile> list = new HashSet<VirtualFile>();
    addFiles(rootDirectory, list);
    for (VirtualFile file : list) {
      final String url = file.getUrl();
      relativeUrls.add(url.substring(length));
    }
    return relativeUrls;
  }

  @Nullable
  public static ModuleFileIndex getFileIndex(@NotNull final ModuleRootManager manager){
      return manager.getModule().isDisposed() ? null : manager.getFileIndex();
  }

  public static void searchFileCacheFiles(@NotNull final ModuleRootManager manager, final Collection<VirtualFile> files) {
    final ModuleFileIndex moduleFileIndex = getFileIndex(manager);
    if (moduleFileIndex == null) {
      return;
    }
    moduleFileIndex.iterateContent(new ContentIterator() {
      public boolean processFile(VirtualFile fileOrDir) {
        if (isPhpFile(fileOrDir)) {
          files.add(fileOrDir);
        }
        return true;
      }
    });
  }


}
