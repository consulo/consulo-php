package net.jay.plugins.php.cache;

import java.io.Serializable;

import net.jay.plugins.php.cache.psi.LightPhpFile;
import net.jay.plugins.php.util.VirtualFileUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.reference.SoftReference;

/**
 * @author jay
 * @date May 21, 2008 1:28:36 PM
 */
public class PhpFileInfo implements Serializable
{
	private final String url;
	private final long timestamp;
	private transient Project project;
	private LightPhpFile lightFile;

	private transient SoftReference<VirtualFile> fileRef;

	public PhpFileInfo(final String url, final long timestamp, @Nullable final Project project)
	{
		this.url = url;
		this.timestamp = timestamp;
		this.project = project;
	}

	@NotNull
	public Project getProject()
	{
		return project;
	}

	public void setProject(@NotNull Project project)
	{
		this.project = project;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	@NotNull
	public String getUrl()
	{
		return url;
	}

	@Nullable
	public String getFileDirectoryUrl()
	{
		return VirtualFileUtil.getParentDir(url);
	}

	@NotNull
	public LightPhpFile getLightPhpFile()
	{
		return lightFile;
	}

	public void setLightPhpFile(@NotNull final LightPhpFile lightFile)
	{
		this.lightFile = lightFile;
	}

	public String toString()
	{
		return getUrl() + " [" + timestamp + "]";
	}

	@Nullable
	public VirtualFile getVirtualFile()
	{
		VirtualFile file;
		if(fileRef == null || (file = fileRef.get()) == null)
		{
			file = VirtualFileManager.getInstance().findFileByUrl(url);
			fileRef = new SoftReference<VirtualFile>(file);
		}
		return file;
	}
}
