package consulo.php.module.extension;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import consulo.php.PhpLanguageLevel;

import javax.annotation.Nullable;
import com.intellij.openapi.projectRoots.Sdk;
import consulo.annotations.RequiredDispatchThread;
import consulo.module.extension.MutableModuleExtensionWithSdk;
import consulo.module.extension.MutableModuleInheritableNamedPointer;
import consulo.roots.ModuleRootLayer;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpMutableModuleExtension extends PhpModuleExtension implements MutableModuleExtensionWithSdk<PhpModuleExtension>
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

	@RequiredDispatchThread
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
	public boolean isModified(@Nonnull PhpModuleExtension extension)
	{
		return isModifiedImpl(extension) || !extension.getInheritableLanguageLevel().equals
				(getInheritableLanguageLevel());
	}
}
