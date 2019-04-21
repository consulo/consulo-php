package consulo.php.run.script;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;

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
