package org.consulo.php.module.extension;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

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

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable) {
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(new ModuleExtensionWithSdkPanel(this, runnable), BorderLayout.NORTH);
		return jPanel;
	}

	@Override
	public void setEnabled(boolean b) {
		myIsEnabled = b;
	}

	@Override
	public boolean isModified() {
		return isModifiedImpl(myOriginalExtension);
	}

	@Override
	public void commit() {
		myOriginalExtension.commit(this);
	}
}
