package consulo.php.module.extension;

import consulo.php.PhpLanguageLevel;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import consulo.module.extension.impl.ModuleInheritableNamedPointerImpl;
import consulo.util.pointers.NamedPointer;
import lombok.val;

/**
 * @author VISTALL
 * @see consulo.java.module.extension.LanguageLevelModuleInheritableNamedPointerImpl
 * @since 07.07.13.
 */
public class LanguageLevelModuleInheritableNamedPointerImpl extends ModuleInheritableNamedPointerImpl<PhpLanguageLevel>
{
	private final String myKey;

	public LanguageLevelModuleInheritableNamedPointerImpl(@NotNull Project project, @NotNull String id)
	{
		super(project, "language-level");
		myKey = id;
	}

	@Override
	public String getItemNameFromModule(@NotNull Module module)
	{
		val extension = (PhpModuleExtension) ModuleUtilCore.getExtension(module, myKey);
		if(extension != null)
		{
			return extension.getLanguageLevel().getName();
		}
		return null;
	}

	@Override
	public PhpLanguageLevel getItemFromModule(@NotNull Module module)
	{
		val extension = (PhpModuleExtension) ModuleUtilCore.getExtension(module, myKey);
		if(extension != null)
		{
			return extension.getLanguageLevel();
		}
		return null;
	}

	@NotNull
	@Override
	public NamedPointer<PhpLanguageLevel> getPointer(@NotNull Project project, @NotNull String name)
	{
		return PhpLanguageLevel.valueOf(name);
	}

	@Override
	public PhpLanguageLevel getDefaultValue()
	{
		return PhpLanguageLevel.HIGHEST;
	}
}
