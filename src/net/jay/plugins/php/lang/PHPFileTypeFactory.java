package net.jay.plugins.php.lang;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.consulo.php.vfs.PharFileType;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 15:22/04.07.13
 */
public class PHPFileTypeFactory extends FileTypeFactory
{
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer)
	{
		for(String extention : PHPFileType.EXTENTIONS)
		{
			fileTypeConsumer.consume(PHPFileType.INSTANCE, extention);
		}
		fileTypeConsumer.consume(PharFileType.INSTANCE);
	}
}
