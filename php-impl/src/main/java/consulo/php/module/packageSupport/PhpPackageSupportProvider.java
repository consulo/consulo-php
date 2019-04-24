package consulo.php.module.packageSupport;

import javax.annotation.Nonnull;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiManager;
import consulo.module.extension.ModuleExtension;
import consulo.php.lang.psi.impl.PhpPackageImpl;
import consulo.php.module.extension.PhpModuleExtension;
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
	public boolean isSupported(@Nonnull ModuleExtension moduleExtension)
	{
		return moduleExtension instanceof PhpModuleExtension;
	}

	@Override
	public boolean isValidPackageName(@Nonnull Module module, @Nonnull String packageName)
	{
		return true;
	}

	@Nonnull
	@Override
	public PsiPackage createPackage(@Nonnull PsiManager psiManager, @Nonnull PsiPackageManager packageManager, @Nonnull Class<? extends ModuleExtension> extensionClass, @Nonnull String packageName)
	{
		return new PhpPackageImpl(psiManager, packageManager, extensionClass, packageName);
	}
}
