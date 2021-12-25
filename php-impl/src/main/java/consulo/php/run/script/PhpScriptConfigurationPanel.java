package consulo.php.run.script;

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
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.php.lang.PhpFileType;
import consulo.awt.TargetAWT;
import consulo.ide.ui.FileChooserTextBoxBuilder;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfigurationPanel extends CommonProgramParametersPanel
{
	private final FileChooserTextBoxBuilder.Controller myController;
	private LabeledComponent<JComponent> myScriptFileComponent;
	private LabeledComponent<ComboBox<Module>> myModuleComponent;

	@RequiredUIAccess
	public PhpScriptConfigurationPanel(@Nonnull Project project)
	{
		super(false);

		FileChooserTextBoxBuilder scriptPathBuilder = FileChooserTextBoxBuilder.create(project);
		scriptPathBuilder.fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileDescriptor(PhpFileType.INSTANCE));

		myController = scriptPathBuilder.build();

		myScriptFileComponent = LabeledComponent.create((JComponent) TargetAWT.to(myController.getComponent()), "Script Path");

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

		myController.setValue(FileUtil.toSystemDependentName(StringUtil.notNullize(phpScriptConfiguration.SCRIPT_PATH)));
		myModuleComponent.getComponent().setSelectedItem(((PhpScriptConfiguration) configuration).getConfigurationModule().getModule());
	}

	@Override
	public void applyTo(CommonProgramRunConfigurationParameters configuration)
	{
		super.applyTo(configuration);

		PhpScriptConfiguration phpScriptConfiguration = (PhpScriptConfiguration) configuration;

		phpScriptConfiguration.SCRIPT_PATH = FileUtil.toSystemIndependentName(myController.getValue());

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
