package consulo.php.impl.run.script;

import com.jetbrains.php.lang.PhpFileType;
import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.php.localize.PhpLocalize;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;

/**
 * @author VISTALL
 * @since 2026-10-23
 */
class PhpScriptPathFragment implements RunConfigurationFragment<PhpScriptConfiguration> {
    @Override
    public String getId() {
        return "phpScriptPath";
    }

    @Override
    public LocalizeValue getName() {
        return PhpLocalize.phpScriptConfigurationScriptPathLabel();
    }

    @Override
    public boolean isRemovable() {
        return false;
    }

    @RequiredUIAccess
    @Override
    public RunConfigurationFragmentEditor<PhpScriptConfiguration> createEditor(Project project, Disposable parent) {
        FileChooserTextBoxBuilder builder = FileChooserTextBoxBuilder.create(project);
        builder.fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFileDescriptor(PhpFileType.INSTANCE));
        builder.uiDisposable(parent);
        FileChooserTextBoxBuilder.Controller scriptPath = builder.build();

        return new RunConfigurationFragmentEditor<>() {
            @RequiredUIAccess
            @Override
            public void addRows(RunConfigurationFragmentLayout layout) {
                layout.addLabeled(PhpLocalize.phpScriptConfigurationScriptPathLabel(), scriptPath.getComponent());
            }

            @RequiredUIAccess
            @Override
            public void reset(PhpScriptConfiguration target) {
                scriptPath.setValue(FileUtil.toSystemDependentName(StringUtil.notNullize(target.getScriptPath())), false);
            }

            @RequiredUIAccess
            @Override
            public void apply(PhpScriptConfiguration target) {
                target.setScriptPath(FileUtil.toSystemIndependentName(StringUtil.notNullize(scriptPath.getValue())));
            }
        };
    }
}
