package net.jay.plugins.php.cache;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.roots.ModuleRootManager;

import java.util.Collection;

/**
 * @author jay
 * @date May 30, 2008 5:01:46 PM
 */
public interface CacheScannerFilesProvider {

  public void scanAndAdd(final String[] rootUrls,
                         final Collection<VirtualFile> files,
                         final ModuleRootManager moduleRootManager);
}
