package org.consulo.php.module.extension;

import org.consulo.module.extension.ModuleInheritableNamedPointer;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.consulo.php.PhpLanguageLevel;
import org.consulo.php.sdk.PhpSdkType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpModuleExtension extends ModuleExtensionWithSdkImpl<PhpModuleExtension>
{
	protected LanguageLevelModuleInheritableNamedPointerImpl myLanguageLevel;

	public PhpModuleExtension(@NotNull String id, @NotNull Module module)
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

	@Override
	protected Class<? extends SdkType> getSdkTypeClass()
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
