package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.php.localize.PhpLocalize;
import consulo.virtualFileSystem.fileType.HashBangFileTypeDetector;

/**
 * @author VISTALL
 * @since 16:50/04.07.13
 */
@ExtensionImpl
public class PhpFileDetector extends HashBangFileTypeDetector {
    public PhpFileDetector() {
        super(PhpFileType.INSTANCE, "php", PhpLocalize.filetypeDescription());
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
