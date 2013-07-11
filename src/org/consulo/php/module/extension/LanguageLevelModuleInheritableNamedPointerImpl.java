package org.consulo.php.module.extension;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import org.consulo.module.extension.ModuleExtensionProvider;
import org.consulo.module.extension.ModuleExtensionProviderEP;
import org.consulo.module.extension.impl.ModuleInheritableNamedPointerImpl;
import org.consulo.php.PhpLanguageLevel;
import org.consulo.util.pointers.NamedPointer;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 07.07.13.
 *
 * @see org.consulo.java.platform.module.extension.LanguageLevelModuleInheritableNamedPointerImpl
 */
public class LanguageLevelModuleInheritableNamedPointerImpl extends ModuleInheritableNamedPointerImpl<PhpLanguageLevel> {
	private final Class<PhpModuleExtension> myExtensionClass;

	public LanguageLevelModuleInheritableNamedPointerImpl(@NotNull Project project, @NotNull String id) {
		super(project, "language-level");
		final ModuleExtensionProvider<?, ?> provider = ModuleExtensionProviderEP.findProvider(id);
		assert provider != null;
		myExtensionClass = (Class<PhpModuleExtension>) provider.getImmutableClass();
	}

	@Override
	public String getItemNameFromModule(@NotNull Module module) {
		final PhpModuleExtension extension = ModuleUtilCore.getExtension(module, myExtensionClass);
		if (extension != null) {
			return extension.getLanguageLevel().getName();
		}
		return null;
	}

	@Override
	public PhpLanguageLevel getItemFromModule(@NotNull Module module) {
		final PhpModuleExtension extension = ModuleUtilCore.getExtension(module, myExtensionClass);
		if (extension != null) {
			return extension.getLanguageLevel();
		}
		return null;
	}

	@NotNull
	@Override
	public NamedPointer<PhpLanguageLevel> getPointer(@NotNull Project project, @NotNull String name) {
		return PhpLanguageLevel.valueOf(name);
	}

	@Override
	public PhpLanguageLevel getDefaultValue() {
		return PhpLanguageLevel.HIGHEST;
	}
}
