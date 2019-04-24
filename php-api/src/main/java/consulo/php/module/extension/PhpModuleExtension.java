package consulo.php.module.extension;

import javax.annotation.Nonnull;

import consulo.module.extension.ModuleExtensionWithSdk;
import consulo.php.PhpLanguageLevel;

/**
 * @author VISTALL
 * @since 2019-04-24
 */
public interface PhpModuleExtension<T extends ModuleExtensionWithSdk<T>> extends ModuleExtensionWithSdk<T>
{
	@Nonnull
	PhpLanguageLevel getLanguageLevel();
}
