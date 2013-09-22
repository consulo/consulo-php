package org.consulo.php.vfs;

import org.jetbrains.annotations.NotNull;
import com.intellij.ide.highlighter.ArchiveFileType;

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

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "phar";
	}

	@NotNull
	@Override
	public String getName()
	{
		return "PHAR_ARCHIVE";
	}
}
