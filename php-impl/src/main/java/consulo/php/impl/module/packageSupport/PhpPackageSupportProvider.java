package consulo.php.impl.module.packageSupport;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiManager;
import consulo.language.psi.PsiPackage;
import consulo.language.psi.PsiPackageManager;
import consulo.language.psi.PsiPackageSupportProvider;
import consulo.module.Module;
import consulo.module.extension.ModuleExtension;
import consulo.php.impl.lang.psi.impl.PhpPackageImpl;
import consulo.php.module.extension.PhpModuleExtension;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 11.10.2015
 */
@ExtensionImpl
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
