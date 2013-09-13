package org.consulo.php;

import com.intellij.lang.Language;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutor;
import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.module.extension.PhpModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpLanguageSubstitutor extends LanguageSubstitutor {
	@Nullable
	@Override
	public Language getLanguage(@NotNull VirtualFile virtualFile, @NotNull Project project) {
		Module moduleForFile = ModuleUtilCore.findModuleForFile(virtualFile, project);
		if(moduleForFile == null) {
			return null;
		}

		if(ModuleUtilCore.getExtension(moduleForFile, PhpModuleExtension.class) == null) {
			return null;
		}

		return PhpLanguage.INSTANCE;
	}
}
