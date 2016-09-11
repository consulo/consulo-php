package consulo.php.module;

import consulo.php.module.extension.PhpModuleExtension;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.ModuleExtensionProviderEP;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public class PhpModuleExtensionUtil
{
	/**
	 * Return if the module extension is single(not mixin)
	 *
	 * @param module
	 * @return
	 */
	public static boolean isSingleModuleExtension(Module module)
	{
		for(ModuleExtensionProviderEP ep : ModuleExtensionProviderEP.EP_NAME.getExtensions())
		{
			if(ep.parentKey != null)
			{
				continue;
			}

			ModuleExtension extension = ModuleUtilCore.getExtension(module, ep.getKey());
			if(extension != null && !(extension instanceof PhpModuleExtension))
			{
				return false;
			}
		}
		return true;
	}
}
