package org.consulo.php.vfs;

import java.io.File;

import org.consulo.lombok.annotations.Logger;
import org.eclipse.php.internal.core.phar.PharFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.vfs.ArchiveFile;
import com.intellij.openapi.vfs.ArchiveFileSystem;
import com.intellij.openapi.vfs.impl.archive.ArchiveHandlerBase;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
@Logger
public class PharHandler extends ArchiveHandlerBase
{

	public PharHandler(@NotNull ArchiveFileSystem fileSystem, @NotNull String path)
	{
		super(fileSystem, path);
	}

	@Override
	@Nullable
	protected ArchiveFile createArchiveFile()
	{
		final File originalFile = getOriginalFile();
		try
		{
			return new PharFile(getMirrorFile(originalFile));
		}
		catch(Exception e)
		{
			LOGGER.warn(e.getMessage() + ": " + originalFile.getPath(), e);
			return null;
		}
	}
}