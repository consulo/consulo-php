package consulo.php.module.util;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-08-20
 */
public class PhpModuleExtensionUtil
{
	@Nonnull
	@RequiredReadAction
	public static PhpLanguageLevel getLanguageLevel(@Nullable Module module)
	{
		if(module == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}

		PhpModuleExtension extension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);
		return extension == null ? PhpLanguageLevel.HIGHEST : extension.getLanguageLevel();
	}

	@Nonnull
	@RequiredReadAction
	public static PhpLanguageLevel getLanguageLevel(@Nullable PsiElement element)
	{
		if(element == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}

		PhpModuleExtension extension = ModuleUtilCore.getExtension(element, PhpModuleExtension.class);
		return extension == null ? PhpLanguageLevel.HIGHEST : extension.getLanguageLevel();
	}
}
