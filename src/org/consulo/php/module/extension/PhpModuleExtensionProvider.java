package org.consulo.php.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.consulo.php.PhpIcons2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public class PhpModuleExtensionProvider implements ModuleExtensionProvider<PhpModuleExtension, PhpMutableModuleExtension>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return PhpIcons2.Php;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "PHP";
	}

	@NotNull
	@Override
	public Class<PhpModuleExtension> getImmutableClass()
	{
		return PhpModuleExtension.class;
	}

	@NotNull
	@Override
	public PhpModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new PhpModuleExtension(s, module);
	}

	@NotNull
	@Override
	public PhpMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull PhpModuleExtension phpModuleExtension)
	{
		return new PhpMutableModuleExtension(s, module, phpModuleExtension);
	}
}
