package consulo.php.lang;

import com.jetbrains.php.lang.PhpFileType;
import consulo.php.vfs.PharFileType;
import javax.annotation.Nonnull;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

/**
 * @author VISTALL
 * @since 15:22/04.07.13
 */
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
