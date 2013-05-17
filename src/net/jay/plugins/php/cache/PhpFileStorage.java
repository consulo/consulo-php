package net.jay.plugins.php.cache;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.HashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author jay
 * @date May 21, 2008 1:31:25 PM
 */
public class PhpFileStorage implements Serializable {

  private static final long serialVersionUID = 1L;

  public enum FileStatus {
    NOT_FOUND,
    OBSOLETTE,
    UP_TO_DATE
  }

  // Info about all the files.
  private final Map<String, PhpFileInfo> myUrl2FileInfoMap = new HashMap<String, PhpFileInfo>();

  public synchronized void addInfo(@NotNull final PhpFileInfo info) {
    myUrl2FileInfoMap.put(info.getUrl(), info);
  }

  public synchronized void init(@NotNull final Project project) {
    final Collection<PhpFileInfo> infos = myUrl2FileInfoMap.values();
    for (PhpFileInfo info : infos) {
      if (info != null) {
        info.setProject(project);
      }
    }
  }

  public synchronized PhpFileInfo getInfoByUrl(@NotNull final String url) {
    return myUrl2FileInfoMap.get(url);
  }

  @NotNull
  public synchronized Set<String> getAllUrls() {
    return new HashSet<String>(myUrl2FileInfoMap.keySet());
  }

  @Nullable
  public synchronized PhpFileInfo removeInfoByUrl(@NotNull final String url) {
    return myUrl2FileInfoMap.remove(url);
  }

  public FileStatus getFileStatus(@NotNull final VirtualFile file) {
    final PhpFileInfo fileInfo = getInfoByUrl(file.getUrl());
    if (fileInfo == null) {
      return FileStatus.NOT_FOUND;
    }
    if (fileInfo.getTimestamp() < file.getTimeStamp()) {
      return FileStatus.OBSOLETTE;
    }
    return FileStatus.UP_TO_DATE;
  }


  public synchronized boolean containsUrl(@NotNull final String url) {
    return myUrl2FileInfoMap.containsKey(url);
  }

  public synchronized void addUrl(@NotNull String url) {
    if (!containsUrl(url)) {
      myUrl2FileInfoMap.put(url, null);
    }
  }
}
