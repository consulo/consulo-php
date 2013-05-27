package net.jay.plugins.php.cache;

import java.util.Collection;

import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author jay
 * @date May 30, 2008 5:01:46 PM
 */
public interface CacheScannerFilesProvider
{

	public void scanAndAdd(final String[] rootUrls, final Collection<VirtualFile> files, final ModuleRootManager moduleRootManager);
}
