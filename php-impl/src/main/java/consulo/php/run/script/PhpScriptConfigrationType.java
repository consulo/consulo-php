package consulo.php.run.script;

import javax.annotation.Nonnull;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.php.PhpIcons;
import consulo.php.module.extension.PhpModuleExtension;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfigrationType extends ConfigurationTypeBase
{
	@Nonnull
	public static PhpScriptConfigrationType getInstance()
	{
		return CONFIGURATION_TYPE_EP.findExtensionOrFail(PhpScriptConfigrationType.class);
	}

	private PhpScriptConfigrationType()
	{
		super("PhpScriptConfigrationType", "PHP Script", "", PhpIcons.Php);

		addFactory(new ConfigurationFactoryEx(this)
		{
			@Override
			public RunConfiguration createTemplateConfiguration(Project project)
			{
				return new PhpScriptConfiguration(project, this);
			}

			@Override
			public boolean isApplicable(@Nonnull Project project)
			{
				return ModuleExtensionHelper.getInstance(project).hasModuleExtension(PhpModuleExtension.class);
			}
		});
	}
}
