package consulo.php.composer;

import javax.annotation.Nonnull;

import com.intellij.openapi.fileTypes.ExactFileNameMatcher;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import consulo.json.JsonFileType;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class ComposerFileTypeFactory extends FileTypeFactory
{
	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer)
	{
		fileTypeConsumer.consume(JsonFileType.INSTANCE, new ExactFileNameMatcher("composer.json"));
		fileTypeConsumer.consume(JsonFileType.INSTANCE, new ExactFileNameMatcher("composer.lock"));
	}
}
