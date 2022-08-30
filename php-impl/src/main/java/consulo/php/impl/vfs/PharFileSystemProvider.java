package consulo.php.impl.vfs;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.archive.ArchiveFile;
import consulo.virtualFileSystem.archive.ArchiveFileSystemProvider;
import org.eclipse.php.internal.core.phar.PharFile;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
@ExtensionImpl
public class PharFileSystemProvider implements ArchiveFileSystemProvider
{
	public static final String PROTOCOL = "phar";

	@Nonnull
	@Override
	public String getProtocol()
	{
		return PROTOCOL;
	}

	@Nonnull
	@Override
	public ArchiveFile createArchiveFile(@Nonnull String path) throws IOException
	{
		return new PharFile(new File(path));
	}
}