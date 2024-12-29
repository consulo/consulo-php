package consulo.php.impl.vfs;

import consulo.localize.LocalizeValue;
import consulo.php.localize.PhpLocalize;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.archive.ArchiveFileType;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 13.07.13.
 */
public class PharFileType extends ArchiveFileType
{
	public static final PharFileType INSTANCE = new PharFileType();

	private PharFileType()
	{
		super(VirtualFileManager.getInstance());
	}

	@Nonnull
	@Override
	public String getProtocol()
	{
		return PharFileSystemProvider.PROTOCOL;
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
