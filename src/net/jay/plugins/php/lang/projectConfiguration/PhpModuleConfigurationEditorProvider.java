package net.jay.plugins.php.lang.projectConfiguration;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.roots.ui.configuration.ContentEntriesEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;

/**
 * @author Maxim
 */
public class PhpModuleConfigurationEditorProvider implements ModuleConfigurationEditorProvider, ModuleComponent
{
	public ModuleConfigurationEditor[] createEditors(ModuleConfigurationState state)
	{
		final Module module = state.getRootModel().getModule();
		if(module.getModuleType() != PhpModuleType.getInstance())
			return new ModuleConfigurationEditor[0];

		return new ModuleConfigurationEditor[]{
				new ContentEntriesEditor(state.getProject(), module.getName(), state.getRootModel(), state.getModulesProvider())
		};
	}

	public void projectOpened()
	{
	}

	public void projectClosed()
	{
	}

	public void moduleAdded()
	{
	}

	@NonNls
	@NotNull
	public String getComponentName()
	{
		return "PhpSupport.ModuleEditorProvider";
	}

	public void initComponent()
	{
	}

	public void disposeComponent()
	{
	}
}
