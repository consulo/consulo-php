package consulo.php.run.script;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import com.intellij.application.options.ModuleListCellRenderer;
import com.intellij.execution.CommonProgramRunConfigurationParameters;
import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpFileType;
import consulo.php.module.extension.PhpModuleExtension;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfigurationPanel extends CommonProgramParametersPanel
{
	private LabeledComponent<TextFieldWithBrowseButton> myScriptFileComponent;
	private LabeledComponent<ComboBox<Module>> myModuleComponent;

	public PhpScriptConfigurationPanel(@Nonnull Project project)
	{
		super(false);

		TextFieldWithBrowseButton component = new TextFieldWithBrowseButton();
		component.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(PhpFileType.INSTANCE), project));
		myScriptFileComponent = LabeledComponent.create(component, "Script Path");

		List<Module> moduleList = new ArrayList<>();
		for(Module module : ModuleManager.getInstance(project).getModules())
		{
			PhpModuleExtension<?> extension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);
			if(extension != null)
			{
				moduleList.add(module);
			}
		}

		ComboBox<Module> moduleBox = new ComboBox<>(ContainerUtil.toArray(moduleList, Module.EMPTY_ARRAY));
		moduleBox.setRenderer(new ModuleListCellRenderer());
		myModuleComponent = LabeledComponent.create(moduleBox, "Module");
		init();
	}

	@Override
	protected void setupAnchor()
	{
		super.setupAnchor();

		myScriptFileComponent.setAnchor(myAnchor);
		myModuleComponent.setAnchor(myAnchor);
	}

	@Override
	public void reset(CommonProgramRunConfigurationParameters configuration)
	{
		super.reset(configuration);

		PhpScriptConfiguration phpScriptConfiguration = (PhpScriptConfiguration) configuration;

		myScriptFileComponent.getComponent().setText(FileUtil.toSystemDependentName(phpScriptConfiguration.SCRIPT_PATH));
		myModuleComponent.getComponent().setSelectedItem(((PhpScriptConfiguration) configuration).getConfigurationModule().getModule());
	}

	@Override
	public void applyTo(CommonProgramRunConfigurationParameters configuration)
	{
		super.applyTo(configuration);

		PhpScriptConfiguration phpScriptConfiguration = (PhpScriptConfiguration) configuration;

		phpScriptConfiguration.SCRIPT_PATH = FileUtil.toSystemIndependentName(myScriptFileComponent.getComponent().getText());

		Object selectedItem = myModuleComponent.getComponent().getSelectedItem();
		if(selectedItem instanceof Module)
		{
			((PhpScriptConfiguration) configuration).getConfigurationModule().setModuleName(((Module) selectedItem).getName());
		}
	}

	@Override
	public void setAnchor(JComponent anchor)
	{
		super.setAnchor(anchor);

		myScriptFileComponent.setAnchor(anchor);
	}

	@Override
	protected void addComponents()
	{
		add(myScriptFileComponent);

		super.addComponents();

		add(myModuleComponent);
	}
}
