package consulo.php.vfs;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.eclipse.php.internal.core.phar.PharFile;
import consulo.vfs.impl.archive.ArchiveFile;
import consulo.vfs.impl.archive.ArchiveFileSystemBase;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
public class PharFileSystem extends ArchiveFileSystemBase
{
	public static final String PROTOCOL = "phar";

	public PharFileSystem()
	{
		super(PROTOCOL);
	}

	@Nonnull
	@Override
	public ArchiveFile createArchiveFile(@Nonnull String path) throws IOException
	{
		return new PharFile(new File(path));
	}
}