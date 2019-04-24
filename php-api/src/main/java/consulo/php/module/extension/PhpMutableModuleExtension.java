package consulo.php.module.extension;

import consulo.module.extension.MutableModuleExtensionWithSdk;

/**
 * @author VISTALL
 * @since 2019-04-24
 */
public interface PhpMutableModuleExtension<T extends PhpModuleExtension<T>> extends MutableModuleExtensionWithSdk<T>, PhpModuleExtension<T>
{
}
