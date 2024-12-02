package consulo.php.impl.module.extension;

import consulo.annotation.component.ExtensionImpl;
import consulo.localize.LocalizeValue;
import consulo.module.content.layer.ModuleExtensionProvider;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.MutableModuleExtension;
import consulo.php.icon.PhpIconGroup;
import consulo.ui.image.Image;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 30-Aug-22
 */
@ExtensionImpl
public class PhpModuleExtensionProvider implements ModuleExtensionProvider<PhpModuleExtensionImpl>
{
	@Nonnull
	@Override
	public String getId()
	{
		return "php";
	}

	@Nonnull
	@Override
	public LocalizeValue getName()
	{
		return LocalizeValue.localizeTODO("PHP");
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return PhpIconGroup.php();
	}

	@Nonnull
	@Override
	public ModuleExtension<PhpModuleExtensionImpl> createImmutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new PhpModuleExtensionImpl(getId(), moduleRootLayer);
	}

	@Nonnull
	@Override
	public MutableModuleExtension<PhpModuleExtensionImpl> createMutableExtension(@Nonnull ModuleRootLayer moduleRootLayer)
	{
		return new PhpMutableModuleExtensionImpl(getId(), moduleRootLayer);
	}
}
