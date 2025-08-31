package consulo.php.impl.run.script;

import com.jetbrains.php.lang.PhpFileType;
import consulo.execution.CommonProgramRunConfigurationParameters;
import consulo.execution.ui.awt.CommonProgramParametersPanel;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.ui.awt.ModuleListCellRenderer;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.awt.ComboBox;
import consulo.ui.ex.awt.LabeledComponent;
import consulo.ui.ex.awtUnsafe.TargetAWT;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import jakarta.annotation.Nonnull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfigurationPanel extends CommonProgramParametersPanel {
    private final FileChooserTextBoxBuilder.Controller myController;
    private LabeledComponent<JComponent> myScriptFileComponent;
    private LabeledComponent<ComboBox<Module>> myModuleComponent;

    @RequiredUIAccess
    public PhpScriptConfigurationPanel(@Nonnull Project project) {
        super(false);

        FileChooserTextBoxBuilder scriptPathBuilder = FileChooserTextBoxBuilder.create(project);
        scriptPathBuilder.fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileDescriptor(PhpFileType.INSTANCE));

        myController = scriptPathBuilder.build();

        myScriptFileComponent = LabeledComponent.create((JComponent) TargetAWT.to(myController.getComponent()), "Script Path");

        List<Module> moduleList = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            PhpModuleExtension<?> extension = ModuleUtilCore.getExtension(module, PhpModuleExtension.class);
            if (extension != null) {
                moduleList.add(module);
            }
        }

        ComboBox<Module> moduleBox = new ComboBox<>(moduleList.toArray(Module.EMPTY_ARRAY));
        moduleBox.setRenderer(new ModuleListCellRenderer());
        myModuleComponent = LabeledComponent.create(moduleBox, "Module");
        init();
    }

    @Override
    protected void setupAnchor() {
        super.setupAnchor();

        myScriptFileComponent.setAnchor(myAnchor);
        myModuleComponent.setAnchor(myAnchor);
    }

    @RequiredUIAccess
    @Override
    public void reset(CommonProgramRunConfigurationParameters configuration) {
        super.reset(configuration);

        PhpScriptConfiguration phpScriptConfiguration = (PhpScriptConfiguration) configuration;

        myController.setValue(FileUtil.toSystemDependentName(StringUtil.notNullize(phpScriptConfiguration.SCRIPT_PATH)));
        myModuleComponent.getComponent().setSelectedItem(((PhpScriptConfiguration) configuration).getConfigurationModule().getModule());
    }

    @RequiredUIAccess
    @Override
    public void applyTo(CommonProgramRunConfigurationParameters configuration) {
        super.applyTo(configuration);

        PhpScriptConfiguration phpScriptConfiguration = (PhpScriptConfiguration) configuration;

        phpScriptConfiguration.SCRIPT_PATH = FileUtil.toSystemIndependentName(myController.getValue());

        Object selectedItem = myModuleComponent.getComponent().getSelectedItem();
        if (selectedItem instanceof Module) {
            ((PhpScriptConfiguration) configuration).getConfigurationModule().setModuleName(((Module) selectedItem).getName());
        }
    }

    @Override
    public void setAnchor(JComponent anchor) {
        super.setAnchor(anchor);

        myScriptFileComponent.setAnchor(anchor);
    }

    @Override
    protected void addComponents() {
        add(myScriptFileComponent);

        super.addComponents();

        add(myModuleComponent);
    }
}
