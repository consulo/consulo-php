package consulo.php.impl.module.extension;

import consulo.annotation.access.RequiredReadAction;
import consulo.content.bundle.SdkType;
import consulo.module.content.layer.ModuleRootLayer;
import consulo.module.content.layer.extension.ModuleExtensionWithSdkBase;
import consulo.module.extension.ModuleInheritableNamedPointer;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.sdk.PhpSdkType;
import org.jdom.Element;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpModuleExtensionImpl extends ModuleExtensionWithSdkBase<PhpModuleExtensionImpl> implements PhpModuleExtension<PhpModuleExtensionImpl>
{
	protected LanguageLevelModuleInheritableNamedPointerImpl myLanguageLevel;

	public PhpModuleExtensionImpl(@Nonnull String id, @Nonnull ModuleRootLayer layer)
	{
		super(id, layer);
		myLanguageLevel = new LanguageLevelModuleInheritableNamedPointerImpl(layer, id);
	}

	@RequiredReadAction
	@Override
	public void commit(@Nonnull PhpModuleExtensionImpl mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		myLanguageLevel.set(mutableModuleExtension.getInheritableLanguageLevel());
	}

	@Nonnull
	public ModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel()
	{
		return myLanguageLevel;
	}

	@Override
	@Nonnull
	public PhpLanguageLevel getLanguageLevel()
	{
		return myLanguageLevel.get();
	}

	@Nonnull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return PhpSdkType.class;
	}

	@Override
	protected void getStateImpl(@Nonnull Element element)
	{
		super.getStateImpl(element);

		myLanguageLevel.toXml(element);
	}

	@RequiredReadAction
	@Override
	protected void loadStateImpl(@Nonnull Element element)
	{
		super.loadStateImpl(element);

		myLanguageLevel.fromXml(element);
	}
}
