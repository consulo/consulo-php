package consulo.php.run.script;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import consulo.php.module.extension.PhpModuleExtension;

/**
 * @author VISTALL
 * @since 2019-04-21
 */
public class PhpScriptConfigurationProducer extends RunConfigurationProducer<PhpScriptConfiguration>
{
	public PhpScriptConfigurationProducer()
	{
		super(PhpScriptConfigrationType.getInstance());
	}

	@Override
	protected boolean setupConfigurationFromContext(PhpScriptConfiguration configuration, ConfigurationContext context, Ref<PsiElement> sourceElement)
	{
		PhpFile phpFile = PsiTreeUtil.getParentOfType(sourceElement.get(), PhpFile.class);
		if(phpFile == null)
		{
			return false;
		}
		PhpModuleExtension<?> extension = ModuleUtilCore.getExtension(phpFile, PhpModuleExtension.class);
		if(extension == null)
		{
			return false;
		}

		configuration.setName(phpFile.getName());
		configuration.SCRIPT_PATH = FileUtil.toSystemIndependentName(phpFile.getVirtualFile().getPath());
		configuration.getConfigurationModule().setModule(extension.getModule());
		return true;
	}

	@Override
	public boolean isConfigurationFromContext(PhpScriptConfiguration configuration, ConfigurationContext context)
	{
		PhpFile phpFile = PsiTreeUtil.getParentOfType(context.getPsiLocation(), PhpFile.class);
		if(phpFile == null)
		{
			return false;
		}

		VirtualFile file = LocalFileSystem.getInstance().findFileByPath(configuration.SCRIPT_PATH);
		if(file == null)
		{
			return false;
		}

		return Comparing.equal(file, phpFile.getVirtualFile());
	}
}
