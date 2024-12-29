package consulo.php.impl.lang.psi.util;

import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiPackageManager;
import consulo.php.impl.lang.psi.PhpPackage;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpPsiUtil
{
	@Nullable
	public static PhpPackage findPackage(@Nonnull Project project, @Nonnull String name)
	{
		return (PhpPackage) PsiPackageManager.getInstance(project).findPackage(name, PhpModuleExtension.class);
	}

	@Nullable
	public static PhpPackage findPackage(@Nonnull Project project, @Nonnull PsiDirectory psiDirectory)
	{
		return (PhpPackage) PsiPackageManager.getInstance(project).findPackage(psiDirectory, PhpModuleExtension.class);
	}
}
