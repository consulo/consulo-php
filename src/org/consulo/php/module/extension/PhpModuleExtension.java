package org.consulo.php.module.extension;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.SdkType;
import net.jay.plugins.php.lang.projectConfiguration.PhpSdkType;
import org.consulo.module.extension.impl.ModuleExtensionWithSdkImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpModuleExtension extends ModuleExtensionWithSdkImpl<PhpModuleExtension> {
	public PhpModuleExtension(@NotNull String id, @NotNull Module module) {
		super(id, module);
	}

	@Override
	protected Class<? extends SdkType> getSdkTypeClass() {
		return PhpSdkType.class;
	}
}
