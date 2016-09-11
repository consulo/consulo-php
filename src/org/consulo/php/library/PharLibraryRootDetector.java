package org.consulo.php.library;

import org.consulo.php.vfs.PharFileType;
import com.intellij.openapi.roots.libraries.ui.FileTypeBasedRootFilter;
import consulo.roots.types.BinariesOrderRootType;

/**
 * @author VISTALL
 * @since 09.03.14
 */
public class PharLibraryRootDetector extends FileTypeBasedRootFilter
{
	public PharLibraryRootDetector()
	{
		super(BinariesOrderRootType.getInstance(), false, PharFileType.INSTANCE, "Phar library");
	}
}
