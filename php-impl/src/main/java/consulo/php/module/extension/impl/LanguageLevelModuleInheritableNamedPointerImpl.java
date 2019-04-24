package consulo.php.module.extension.impl;

import javax.annotation.Nonnull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import consulo.module.extension.impl.ModuleInheritableNamedPointerImpl;
import consulo.php.PhpLanguageLevel;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.roots.ModuleRootLayer;
import consulo.util.pointers.NamedPointer;

/**
 * @author VISTALL
 * @since 07.07.13
 */
public class LanguageLevelModuleInheritableNamedPointerImpl extends ModuleInheritableNamedPointerImpl<PhpLanguageLevel>
{
	private final String myKey;

	public LanguageLevelModuleInheritableNamedPointerImpl(@Nonnull ModuleRootLayer layer, @Nonnull String id)
	{
		super(layer, "language-level");
		myKey = id;
	}

	@Override
	public String getItemNameFromModule(@Nonnull Module module)
	{
		PhpModuleExtension<?> extension = (PhpModuleExtension<?>) ModuleUtilCore.getExtension(module, myKey);
		if(extension != null)
		{
			return extension.getLanguageLevel().getId();
		}
		return null;
	}

	@Override
	public PhpLanguageLevel getItemFromModule(@Nonnull Module module)
	{
		PhpModuleExtension<?> extension = (PhpModuleExtension<?>) ModuleUtilCore.getExtension(module, myKey);
		if(extension != null)
		{
			return extension.getLanguageLevel();
		}
		return null;
	}

	@Nonnull
	@Override
	public NamedPointer<PhpLanguageLevel> getPointer(@Nonnull ModuleRootLayer layer, @Nonnull String name)
	{
		for(PhpLanguageLevel value : PhpLanguageLevel.VALUES)
		{
			if(value.getId().equals(name))
			{
				return value;
			}
		}
		return PhpLanguageLevel.HIGHEST;
	}

	@Override
	public PhpLanguageLevel getDefaultValue()
	{
		return PhpLanguageLevel.HIGHEST;
	}
}
