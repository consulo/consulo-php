package org.consulo.php.lang;

import org.consulo.php.vfs.PharFileType;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

/**
 * @author VISTALL
 * @since 15:22/04.07.13
 */
public class PhpFileTypeFactory extends FileTypeFactory
{
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer)
	{
		for(String extention : PhpFileType.EXTENTIONS)
		{
			fileTypeConsumer.consume(PhpFileType.INSTANCE, extention);
		}
		fileTypeConsumer.consume(PharFileType.INSTANCE);
	}
}
