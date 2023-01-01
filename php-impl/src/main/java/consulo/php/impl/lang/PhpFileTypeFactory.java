package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.php.impl.vfs.PharFileType;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 15:22/04.07.13
 */
@ExtensionImpl
public class PhpFileTypeFactory extends FileTypeFactory
{
	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer)
	{
		for(String extention : PhpFileType.EXTENTIONS)
		{
			fileTypeConsumer.consume(PhpFileType.INSTANCE, extention);
		}
		fileTypeConsumer.consume(PharFileType.INSTANCE);
	}
}
