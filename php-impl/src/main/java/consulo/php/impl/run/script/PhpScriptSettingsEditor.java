package consulo.php.impl.run.script;

import jakarta.annotation.Nonnull;
import javax.swing.JComponent;

import consulo.configurable.ConfigurationException;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.project.Project;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptSettingsEditor extends SettingsEditor<PhpScriptConfiguration>
{
	private final Project myProject;
	private PhpScriptConfigurationPanel myPanel;

	public PhpScriptSettingsEditor(Project project)
	{
		myProject = project;
	}

	@Nonnull
	@Override
	protected JComponent createEditor()
	{
		return myPanel = new PhpScriptConfigurationPanel(myProject);
	}

	@Override
	protected void resetEditorFrom(PhpScriptConfiguration phpScriptConfiguration)
	{
		myPanel.reset(phpScriptConfiguration);
	}

	@Override
	protected void applyEditorTo(PhpScriptConfiguration phpScriptConfiguration) throws ConfigurationException
	{
		myPanel.applyTo(phpScriptConfiguration);
	}
}
