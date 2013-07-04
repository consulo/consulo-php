package net.jay.plugins.php.lang;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

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
	}
}
