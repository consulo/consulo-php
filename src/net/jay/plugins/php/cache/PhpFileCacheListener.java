package net.jay.plugins.php.cache;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date May 29, 2008 11:11:14 AM
 */
public interface PhpFileCacheListener
{
	/**
	 * File was added to cache
	 *
	 * @param url file url
	 */
	public void fileAdded(@NotNull final String url);

	/**
	 * File was removed from cache
	 *
	 * @param url file url
	 */
	public void fileRemoved(@NotNull final String url);

	/**
	 * File was updated in cache
	 *
	 * @param url file url
	 */
	public void fileUpdated(@NotNull final String url);
}
