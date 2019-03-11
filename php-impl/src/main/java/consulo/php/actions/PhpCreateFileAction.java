package consulo.php.actions;

import consulo.awt.TargetAWT;
import consulo.php.PhpIcons;
import consulo.php.module.extension.PhpModuleExtension;
import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * @author VISTALL
 * @since 05.07.13.
 */
public class PhpCreateFileAction extends CreateFileFromTemplateAction
{
	public PhpCreateFileAction()
	{
		super("Php File", "Create new Php File", TargetAWT.to(PhpIcons.Php));
	}

	@Override
	protected boolean isAvailable(final DataContext dataContext)
	{
		final Project project = dataContext.getData(PlatformDataKeys.PROJECT);
		final IdeView view = dataContext.getData(LangDataKeys.IDE_VIEW);
		if(project == null || view == null || view.getDirectories().length == 0)
		{
			return false;
		}

		final Module module = dataContext.getData(LangDataKeys.MODULE);
		if(module == null)
		{
			return false;
		}

		if(ModuleUtilCore.getExtension(module, PhpModuleExtension.class) == null)
		{
			return false;
		}

		return true;
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder.setTitle("Create File").addKind("File", TargetAWT.to(PhpIcons.Php), "PHP File");
	}

	@Override
	protected String getActionName(PsiDirectory psiDirectory, String s, String s2)
	{
		return "Creating class";
	}
}
