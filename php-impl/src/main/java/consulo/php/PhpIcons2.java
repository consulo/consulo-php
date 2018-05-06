package consulo.php;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import consulo.ui.image.Image;
import consulo.ui.migration.SwingImageRef;

/**
 * @author VISTALL
 * @since 15:29/04.07.13
 */
public interface PhpIcons2
{
	Image Php = IconLoader.findIcon("/icons/fileTypes/php.png");

	SwingImageRef AbstractClass = AllIcons.Nodes.AbstractClass;
	SwingImageRef Class = AllIcons.Nodes.Class;
	SwingImageRef Trait = AllIcons.Nodes.Trait;
	SwingImageRef Interface = AllIcons.Nodes.Interface;
}
