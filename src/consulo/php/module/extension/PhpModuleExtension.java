package consulo.php.module.extension;

import consulo.php.PhpLanguageLevel;
import consulo.php.sdk.PhpSdkType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.projectRoots.SdkType;
import consulo.extension.impl.ModuleExtensionWithSdkImpl;
import consulo.module.extension.ModuleInheritableNamedPointer;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpModuleExtension extends ModuleExtensionWithSdkImpl<PhpModuleExtension>
{
	protected LanguageLevelModuleInheritableNamedPointerImpl myLanguageLevel;

	public PhpModuleExtension(@NotNull String id, @NotNull ModuleRootLayer module)
	{
		super(id, module);
		myLanguageLevel = new LanguageLevelModuleInheritableNamedPointerImpl(module.getProject(), id);
	}

	@Override
	public void commit(@NotNull PhpModuleExtension mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		myLanguageLevel.set(mutableModuleExtension.getInheritableLanguageLevel());
	}

	@NotNull
	public ModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel()
	{
		return myLanguageLevel;
	}

	@NotNull
	public PhpLanguageLevel getLanguageLevel()
	{
		return myLanguageLevel.get();
	}

	@NotNull
	@Override
	public Class<? extends SdkType> getSdkTypeClass()
	{
		return PhpSdkType.class;
	}

	@Override
	protected void getStateImpl(@NotNull Element element)
	{
		super.getStateImpl(element);

		myLanguageLevel.toXml(element);
	}

	@Override
	protected void loadStateImpl(@NotNull Element element)
	{
		super.loadStateImpl(element);

		myLanguageLevel.fromXml(element);
	}
}
