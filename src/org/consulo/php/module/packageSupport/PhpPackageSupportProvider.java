package org.consulo.php.module.packageSupport;

import org.consulo.php.lang.psi.impl.PhpPackageImpl;
import org.consulo.php.module.PhpModuleExtensionUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiManager;
import consulo.module.extension.ModuleExtension;
import consulo.psi.PsiPackage;
import consulo.psi.PsiPackageManager;
import consulo.psi.PsiPackageSupportProvider;

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
