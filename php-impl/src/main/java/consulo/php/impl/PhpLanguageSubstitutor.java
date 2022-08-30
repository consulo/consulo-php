package consulo.php.impl;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.psi.LanguageSubstitutor;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
@ExtensionImpl
public class PhpLanguageSubstitutor extends LanguageSubstitutor
{
	@Nullable
	@Override
	public Language getLanguage(@Nonnull VirtualFile virtualFile, @Nonnull Project project)
	{
		Module moduleForFile = ModuleUtilCore.findModuleForFile(virtualFile, project);
		if(moduleForFile == null)
		{
			return null;
		}

		if(ModuleUtilCore.getExtension(moduleForFile, PhpModuleExtension.class) == null)
		{
			return null;
		}

		return PhpLanguage.INSTANCE;
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
