package consulo.php.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.lang.PhpFileType;

/**
 * @author VISTALL
 * @since 16:50/04.07.13
 */
public class PhpFileDetector implements FileTypeRegistry.FileTypeDetector
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
