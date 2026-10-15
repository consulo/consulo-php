package consulo.php.impl.run.script;

import com.jetbrains.php.lang.PhpFileType;
import consulo.execution.ui.CommonProgramParametersLayout;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.php.localize.PhpLocalize;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import consulo.ui.ComboBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.dialog.DialogService;
import consulo.ui.util.FormBuilder;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfigurationPanel extends CommonProgramParametersLayout<PhpScriptConfiguration> {
    private final Project myProject;
    private FileChooserTextBoxBuilder.@Nullable Controller myScriptPath;
    private @Nullable ComboBox<Module> myModuleBox;

    public PhpScriptConfigurationPanel(Project project) {
        super(project.getApplication().getInstance(DialogService.class));
        myProject = project;
    }

    @Override
    @RequiredUIAccess
    protected void addBefore(FormBuilder builder) {
        FileChooserTextBoxBuilder scriptPathBuilder = FileChooserTextBoxBuilder.create(myProject);
        scriptPathBuilder.fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileDescriptor(PhpFileType.INSTANCE));
        FileChooserTextBoxBuilder.Controller scriptPath = scriptPathBuilder.build();
        myScriptPath = scriptPath;

        builder.addLabeled(PhpLocalize.phpScriptConfigurationScriptPathLabel(), scriptPath.getComponent());
    }

    @Override
    @RequiredUIAccess
    protected void addAfter(FormBuilder builder) {
        List<Module> modules = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(myProject).getModules()) {
            if (ModuleUtilCore.getExtension(module, PhpModuleExtension.class) != null) {
                modules.add(module);
            }
        }

        ComboBox<Module> moduleBox = ComboBox.create(modules);
        moduleBox.setRenderer((presentation, item) -> {
            Module module = item.getValue();
            if (module != null) {
                presentation.withIcon(PlatformIconGroup.nodesModule());
                presentation.append(module.getName());
            }
        });
        myModuleBox = moduleBox;

        builder.addLabeled(PhpLocalize.phpScriptConfigurationModuleLabel(), moduleBox);
    }

    @Override
    @RequiredUIAccess
    public void reset(PhpScriptConfiguration configuration) {
        super.reset(configuration);

        FileChooserTextBoxBuilder.Controller scriptPath = myScriptPath;
        if (scriptPath != null) {
            scriptPath.setValue(FileUtil.toSystemDependentName(StringUtil.notNullize(configuration.SCRIPT_PATH)));
        }

        ComboBox<Module> moduleBox = myModuleBox;
        if (moduleBox != null) {
            moduleBox.setValue(configuration.getConfigurationModule().getModule());
        }
    }

    @Override
    @RequiredUIAccess
    public void apply(PhpScriptConfiguration configuration) {
        super.apply(configuration);

        FileChooserTextBoxBuilder.Controller scriptPath = myScriptPath;
        if (scriptPath != null) {
            configuration.SCRIPT_PATH = FileUtil.toSystemIndependentName(StringUtil.notNullize(scriptPath.getValue()));
        }

        ComboBox<Module> moduleBox = myModuleBox;
        Module module = moduleBox == null ? null : moduleBox.getValue();
        if (module != null) {
            configuration.getConfigurationModule().setModuleName(module.getName());
        }
    }
}
