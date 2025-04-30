package consulo.php.impl.run.script;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.RunConfiguration;
import consulo.module.extension.ModuleExtensionHelper;
import consulo.php.icon.PhpIconGroup;
import consulo.php.localize.PhpLocalize;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;
import jakarta.inject.Inject;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
@ExtensionImpl
public class PhpScriptConfigrationType extends ConfigurationTypeBase {
    @Nonnull
    public static PhpScriptConfigrationType getInstance() {
        return EP_NAME.findExtensionOrFail(PhpScriptConfigrationType.class);
    }

    @Inject
    PhpScriptConfigrationType() {
        super("PhpScriptConfigurationType", PhpLocalize.phpScriptConfigurationName(), PhpIconGroup.filetypesPhp());

        addFactory(new ConfigurationFactory(this) {
            @Override
            public RunConfiguration createTemplateConfiguration(Project project) {
                return new PhpScriptConfiguration(project, this);
            }

            @Override
            public boolean isApplicable(@Nonnull Project project) {
                return ModuleExtensionHelper.getInstance(project).hasModuleExtension(PhpModuleExtension.class);
            }
        });
    }
}
