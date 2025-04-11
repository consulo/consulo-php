package consulo.php.impl.run.script;

import com.jetbrains.php.lang.psi.PhpFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.execution.action.ConfigurationContext;
import consulo.execution.action.RunConfigurationProducer;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.ModuleUtilCore;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.util.io.FileUtil;
import consulo.util.lang.Comparing;
import consulo.util.lang.ref.Ref;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
@ExtensionImpl
public class PhpScriptConfigurationProducer extends RunConfigurationProducer<PhpScriptConfiguration> {
    public PhpScriptConfigurationProducer() {
        super(PhpScriptConfigrationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(
        PhpScriptConfiguration configuration,
        ConfigurationContext context,
        Ref<PsiElement> sourceElement
    ) {
        PhpFile phpFile = PsiTreeUtil.getParentOfType(sourceElement.get(), PhpFile.class, false);
        if (phpFile == null) {
            return false;
        }
        PhpModuleExtension<?> extension = ModuleUtilCore.getExtension(phpFile, PhpModuleExtension.class);
        if (extension == null) {
            return false;
        }

        configuration.setName(phpFile.getName());
        configuration.SCRIPT_PATH = FileUtil.toSystemIndependentName(phpFile.getVirtualFile().getPath());
        configuration.getConfigurationModule().setModule(extension.getModule());
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(PhpScriptConfiguration configuration, ConfigurationContext context) {
        PhpFile phpFile = PsiTreeUtil.getParentOfType(context.getPsiLocation(), PhpFile.class, false);
        if (phpFile == null) {
            return false;
        }

        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(configuration.SCRIPT_PATH);
        if (file == null) {
            return false;
        }

        return Comparing.equal(file, phpFile.getVirtualFile());
    }
}
