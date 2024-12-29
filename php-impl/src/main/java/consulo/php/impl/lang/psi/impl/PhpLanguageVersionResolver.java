package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.psi.PsiElement;
import consulo.language.util.ModuleUtilCore;
import consulo.language.version.LanguageVersion;
import consulo.language.version.LanguageVersionResolver;
import consulo.module.Module;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
@ExtensionImpl
public class PhpLanguageVersionResolver implements LanguageVersionResolver
{
	@RequiredReadAction
	@Nonnull
	@Override
	public LanguageVersion getLanguageVersion(@Nonnull Language language, @Nullable PsiElement psiElement)
	{
		if(psiElement == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}
		return findLanguage(ModuleUtilCore.findModuleForPsiElement(psiElement));
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public LanguageVersion getLanguageVersion(@Nonnull Language language, @Nullable Project project, @Nullable VirtualFile virtualFile)
	{
		if(project == null || virtualFile == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}
		return findLanguage(ModuleUtilCore.findModuleForFile(virtualFile, project));
	}

	@RequiredReadAction
	private static LanguageVersion findLanguage(Module module)
	{
		if(module == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}
		PhpModuleExtension extension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);
		if(extension == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}
		return extension.getLanguageLevel();
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
