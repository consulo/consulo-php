package org.consulo.php.module.packageSupport;

import org.consulo.module.extension.ModuleExtension;
import org.consulo.php.lang.psi.impl.PhpPackageImpl;
import org.consulo.php.module.PhpModuleExtensionUtil;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.consulo.psi.PsiPackageSupportProvider;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiManager;

/**
 * @author VISTALL
 * @since 11.10.2015
 */
public class PhpPackageSupportProvider implements PsiPackageSupportProvider
{
	@Override
	public boolean isSupported(@NotNull ModuleExtension moduleExtension)
	{
		return PhpModuleExtensionUtil.isSingleModuleExtension(moduleExtension.getModule());
	}

	@Override
	public boolean isValidPackageName(@NotNull Module module, @NotNull String packageName)
	{
		return true;
	}

	@NotNull
	@Override
	public PsiPackage createPackage(@NotNull PsiManager psiManager, @NotNull PsiPackageManager packageManager, @NotNull Class<? extends ModuleExtension> extensionClass, @NotNull String packageName)
	{
		return new PhpPackageImpl(psiManager, packageManager, extensionClass, packageName);
	}
}
