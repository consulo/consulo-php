package consulo.php.module.extension.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.intellij.openapi.projectRoots.Sdk;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.php.PhpLanguageLevel;
import consulo.roots.ModuleRootLayer;
import consulo.ui.RequiredUIAccess;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpMutableModuleExtension extends PhpModuleExtensionImpl implements MutableModuleExtensionWithSdk<PhpModuleExtensionImpl>
{
	public PhpMutableModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer module)
	{
		super(id, module);
	}

	@Nonnull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Override
	@Nonnull
	public MutableModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel()
	{
		return myLanguageLevel;
	}

	@RequiredUIAccess
	@Nullable
	@Override
	public JComponent createConfigurablePanel(@Nullable Runnable runnable)
	{
		return new PhpModuleExtensionPanel(this, runnable);
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified(@Nonnull PhpModuleExtensionImpl extension)
	{
		return isModifiedImpl(extension) || !extension.getInheritableLanguageLevel().equals
				(getInheritableLanguageLevel());
	}
}
