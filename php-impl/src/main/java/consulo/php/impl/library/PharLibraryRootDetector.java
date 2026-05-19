package consulo.php.impl.library;

import consulo.annotation.component.ExtensionImpl;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.library.ui.FileTypeBasedRootFilter;
import consulo.localize.LocalizeValue;
import consulo.php.impl.vfs.PharFileType;

/**
 * @author VISTALL
 * @since 09.03.14
 */
@ExtensionImpl
public class PharLibraryRootDetector extends FileTypeBasedRootFilter {
    public PharLibraryRootDetector() {
        super(BinariesOrderRootType.ID, false, PharFileType.INSTANCE, LocalizeValue.localizeTODO("Phar library"));
    }
}
