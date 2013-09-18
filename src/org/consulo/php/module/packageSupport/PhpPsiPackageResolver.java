package org.consulo.php.module.packageSupport;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import org.consulo.module.extension.ModuleExtension;
import org.consulo.php.module.PhpModuleExtensionUtil;
import org.consulo.php.lang.psi.impl.PhpPackageImpl;
import org.consulo.psi.PsiPackage;
import org.consulo.psi.PsiPackageManager;
import org.consulo.psi.PsiPackageResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @see org.consulo.psi.impl.DefaultPsiPackageResolver
 * @since 07.07.13
 */
public class PhpPsiPackageResolver implements PsiPackageResolver {
	@Nullable
	@Override
	public PsiPackage resolvePackage(@NotNull PsiPackageManager packageManager, @NotNull VirtualFile virtualFile,
													 @NotNull Class<? extends ModuleExtension> extensionClass,
													 String qualifiedName) {

		final Module moduleForFile = ModuleUtil.findModuleForFile(virtualFile, packageManager.getProject());
		if (moduleForFile == null) {
			return null;
		}

		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(moduleForFile);

		final ModuleExtension extension = moduleRootManager.getExtension(extensionClass);
		if (extension == null || !PhpModuleExtensionUtil.isSingleModuleExtension(moduleForFile)) {
			return null;
		}

		return new PhpPackageImpl(PsiManager.getInstance(packageManager.getProject()), packageManager, extensionClass, qualifiedName);
	}
}
