package consulo.php.lang.psi.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import consulo.php.lang.psi.PhpPackage;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.psi.PsiPackageManager;

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
