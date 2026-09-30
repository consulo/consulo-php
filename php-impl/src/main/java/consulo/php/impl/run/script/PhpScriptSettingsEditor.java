package consulo.php.impl.run.script;

import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.project.Project;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptSettingsEditor extends SettingsEditor<PhpScriptConfiguration> {
    private final PhpScriptConfigurationPanel myPanel;

    public PhpScriptSettingsEditor(Project project) {
        myPanel = new PhpScriptConfigurationPanel(project);
    }

    @RequiredUIAccess
    @Override
    protected Component createUIComponent() {
        myPanel.build();
        return myPanel.getComponent();
    }

    @RequiredUIAccess
    @Override
    protected void resetEditorFrom(PhpScriptConfiguration phpScriptConfiguration) {
        myPanel.reset(phpScriptConfiguration);
    }

    @RequiredUIAccess
    @Override
    protected void applyEditorTo(PhpScriptConfiguration phpScriptConfiguration) throws ConfigurationException {
        myPanel.apply(phpScriptConfiguration);
    }
}
