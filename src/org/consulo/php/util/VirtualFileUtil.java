package org.consulo.php.util;

import static com.intellij.openapi.util.io.FileUtil.toSystemIndependentName;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

/**
 * @author jay
 * @date May 29, 2008 12:42:02 PM
 */
public class VirtualFileUtil
{

	@NonNls
	public static final char VFS_PATH_SEPARATOR = '/';

	/**
	 * @param dir  directory
	 * @param name file name
	 * @return url for file with name in directory dir
	 */
	public static String constructUrl(@NotNull final VirtualFile dir, final String name)
	{
		return dir.getUrl() + VFS_PATH_SEPARATOR + name;
	}

	/**
	 * @param url Url for virtual file
	 * @return url for parent directory of virtual file
	 */
	@Nullable
	public static String getParentDir(@Nullable final String url)
	{
		if(url == null)
		{
			return null;
		}
		final int index = url.lastIndexOf(VFS_PATH_SEPARATOR);
		return index < 0 ? null : url.substring(0, index);
	}

	/**
	 * Converts OS depended path to VirtualFile url
	 *
	 * @param path Path
	 * @return url
	 */
	@NotNull
	public static String constructLocalUrl(@NotNull final String path)
	{
		return VirtualFileManager.constructUrl(LocalFileSystem.PROTOCOL, toSystemIndependentName(path));
	}

}
