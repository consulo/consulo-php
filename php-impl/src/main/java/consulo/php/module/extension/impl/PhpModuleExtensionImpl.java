package consulo.php.module.extension.impl;

import com.intellij.openapi.projectRoots.SdkType;
import consulo.annotation.access.RequiredReadAction;
import consulo.module.extension.ModuleInheritableNamedPointer;
import consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.sdk.PhpSdkType;
import consulo.roots.ModuleRootLayer;
import org.jdom.Element;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpModuleExtensionImpl extends ModuleExtensionWithSdkImpl<PhpModuleExtensionImpl> implements PhpModuleExtension<PhpModuleExtensionImpl>
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
