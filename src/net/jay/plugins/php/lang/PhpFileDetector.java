package net.jay.plugins.php.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author VISTALL
 * @since 16:50/04.07.13
 */
public class PhpFileDetector implements FileTypeRegistry.FileTypeDetector
{
	@Nullable
	@Override
	public FileType detect(@NotNull VirtualFile virtualFile, @NotNull ByteSequence byteSequence, @Nullable CharSequence charSequence)
	{
		if(FileUtil.isHashBangLine(charSequence, "php"))
		{
			return PHPFileType.INSTANCE;
		}
		return null;
	}
}
