package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.ide.impl.idea.openapi.util.io.FileUtil;
import consulo.util.io.ByteSequence;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileType;
import consulo.virtualFileSystem.fileType.FileTypeDetector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 16:50/04.07.13
 */
@ExtensionImpl
public class PhpFileDetector implements FileTypeDetector
{
	@Nullable
	@Override
	public FileType detect(@Nonnull VirtualFile virtualFile, @Nonnull ByteSequence byteSequence, @Nullable CharSequence charSequence)
	{
		if(FileUtil.isHashBangLine(charSequence, "php"))
		{
			return PhpFileType.INSTANCE;
		}
		return null;
	}

	@Override
	public int getVersion()
	{
		return 1;
	}
}
