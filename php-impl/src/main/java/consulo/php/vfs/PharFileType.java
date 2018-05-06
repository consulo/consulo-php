package consulo.php.vfs;

import javax.annotation.Nonnull;
import consulo.fileTypes.ArchiveFileType;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
public class PharFileType extends ArchiveFileType
{
	public static final PharFileType INSTANCE = new PharFileType();

	@Override
	public String getProtocol()
	{
		return PharFileSystem.PROTOCOL;
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "phar";
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "PHAR_ARCHIVE";
	}
}
