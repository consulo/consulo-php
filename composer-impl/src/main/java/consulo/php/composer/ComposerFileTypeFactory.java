package consulo.php.composer;

import consulo.annotation.component.ExtensionImpl;
import consulo.json.JsonFileType;
import consulo.virtualFileSystem.fileType.FileNameMatcherFactory;
import consulo.virtualFileSystem.fileType.FileTypeConsumer;
import consulo.virtualFileSystem.fileType.FileTypeFactory;
import jakarta.inject.Inject;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
@ExtensionImpl
public class ComposerFileTypeFactory extends FileTypeFactory
{
	public static final String COMPOSER_JSON = "composer.json";
	public static final String COMPOSER_LOCK = "composer.lock";

	private final FileNameMatcherFactory myFileNameMatcherFactory;

	@Inject
	public ComposerFileTypeFactory(FileNameMatcherFactory fileNameMatcherFactory)
	{
		myFileNameMatcherFactory = fileNameMatcherFactory;
	}

	@Override
	public void createFileTypes(@Nonnull FileTypeConsumer fileTypeConsumer)
	{
		fileTypeConsumer.consume(JsonFileType.INSTANCE, myFileNameMatcherFactory.createExactFileNameMatcher(COMPOSER_JSON));
		fileTypeConsumer.consume(JsonFileType.INSTANCE, myFileNameMatcherFactory.createExactFileNameMatcher(COMPOSER_LOCK));
	}
}
