package consulo.php.vfs;

import consulo.fileTypes.ArchiveFileType;
import consulo.localize.LocalizeValue;
import consulo.php.localize.PhpLocalize;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
public class PharFileType extends ArchiveFileType
{
	public static final PharFileType INSTANCE = new PharFileType();

	@Nonnull
	@Override
	public String getProtocol()
	{
		return PharFileSystem.PROTOCOL;
	}

	@Nonnull
	@Override
	public LocalizeValue getDescription()
	{
		return PhpLocalize.pharArchiveFileDescription();
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "phar";
	}

	@Nonnull
	@Override
	public String getId()
	{
		return "PHAR_ARCHIVE";
	}
}
