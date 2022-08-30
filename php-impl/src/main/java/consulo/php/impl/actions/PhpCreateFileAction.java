package consulo.php.impl.actions;

import consulo.dataContext.DataContext;
import consulo.ide.IdeView;
import consulo.ide.action.CreateFileFromTemplateAction;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.language.editor.LangDataKeys;
import consulo.language.editor.PlatformDataKeys;
import consulo.language.psi.PsiDirectory;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.php.icon.PhpIconGroup;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.project.Project;

/**
 * @author VISTALL
 * @since 05.07.13.
 */
public class PhpCreateFileAction extends CreateFileFromTemplateAction
{
	public PhpCreateFileAction()
	{
		super("Php File", "Create new Php File", PhpIconGroup.filetypesPhp());
	}

	@Override
	protected boolean isAvailable(final DataContext dataContext)
	{
		final Project project = dataContext.getData(PlatformDataKeys.PROJECT);
		final IdeView view = dataContext.getData(IdeView.KEY);
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
		builder.setTitle("Create File").addKind("File", PhpIconGroup.filetypesPhp(), "PHP File");
	}

	@Override
	protected String getActionName(PsiDirectory psiDirectory, String s, String s2)
	{
		return "Creating class";
	}
}
