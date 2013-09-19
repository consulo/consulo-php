package org.consulo.php.lang.psi.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.consulo.php.lang.psi.PhpPackage;
import org.consulo.php.module.extension.PhpModuleExtension;
import org.consulo.psi.PsiPackageManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpPsiUtil {
	@Nullable
	public static PhpPackage findPackage(@NotNull Project project, @NotNull String name) {
		return  (PhpPackage) PsiPackageManager.getInstance(project).findPackage(name, PhpModuleExtension.class);
	}

	@Nullable
	public static PhpPackage findPackage(@NotNull Project project, @NotNull PsiDirectory psiDirectory) {
		return  (PhpPackage) PsiPackageManager.getInstance(project).findPackage(psiDirectory, PhpModuleExtension.class);
	}
}
