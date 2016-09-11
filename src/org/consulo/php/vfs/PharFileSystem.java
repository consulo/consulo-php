package org.consulo.php.vfs;

import java.io.File;
import java.io.IOException;

import org.eclipse.php.internal.core.phar.PharFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.ApplicationComponent;
import consulo.vfs.impl.archive.ArchiveFile;
import consulo.vfs.impl.archive.ArchiveFileSystemBase;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
public class PharFileSystem extends ArchiveFileSystemBase implements ApplicationComponent
{
	public static final String PROTOCOL = "phar";

	public PharFileSystem()
	{
		super(PROTOCOL);
	}

	@NotNull
	@Override
	public ArchiveFile createArchiveFile(@NotNull String path) throws IOException
	{
		return new PharFile(new File(path));
	}
}