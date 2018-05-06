package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;

import javax.annotation.Nullable;
import com.intellij.lang.Language;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import consulo.lang.LanguageVersion;
import consulo.lang.LanguageVersionResolver;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpLanguageVersionResolver implements LanguageVersionResolver
{
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

	@Override
	public LanguageVersion getLanguageVersion(@Nonnull Language language, @Nullable Project project, @Nullable VirtualFile virtualFile)
	{
		if(project == null || virtualFile == null)
		{
			return PhpLanguageLevel.HIGHEST;
		}
		return findLanguage(ModuleUtilCore.findModuleForFile(virtualFile, project));
	}

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
}
