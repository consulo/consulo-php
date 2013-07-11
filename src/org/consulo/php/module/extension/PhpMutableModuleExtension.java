package org.consulo.php.module.extension;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.php.PhpLanguageLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpMutableModuleExtension extends PhpModuleExtension implements MutableModuleExtensionWithSdk<PhpModuleExtension> {
	private PhpModuleExtension myOriginalExtension;

	public PhpMutableModuleExtension(@NotNull String id, @NotNull Module module, PhpModuleExtension originalExtension) {
		super(id, module);
		myOriginalExtension = originalExtension;
		commit(originalExtension);
	}

	@NotNull
	@Override
	public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk() {
		return (MutableModuleInheritableNamedPointer<Sdk>) super.getInheritableSdk();
	}

	@Override
	@NotNull
	public MutableModuleInheritableNamedPointer<PhpLanguageLevel> getInheritableLanguageLevel() {
		return myLanguageLevel;
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable) {
		return new PhpModuleExtensionPanel(this, runnable);
	}

	@Override
	public void setEnabled(boolean b) {
		myIsEnabled = b;
	}

	@Override
	public boolean isModified() {
		return isModifiedImpl(myOriginalExtension) || !myOriginalExtension.getInheritableLanguageLevel().equals(getInheritableLanguageLevel());
	}

	@Override
	public void commit() {
		myOriginalExtension.commit(this);
	}
}
