package org.consulo.php.actions;

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
import org.consulo.php.PHPIcons2;
import org.consulo.php.module.extension.PhpModuleExtension;

/**
 * @author VISTALL
 * @since 05.07.13.
 */
public class PhpCreateClassAction extends CreateFileFromTemplateAction {
	public PhpCreateClassAction() {
		super("Php Class", "Create new Php Class", PHPIcons2.Php);
	}

	@Override
	protected boolean isAvailable(final DataContext dataContext) {
		final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
		final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
		if (project == null || view == null || view.getDirectories().length == 0) {
			return false;
		}

		final Module module = LangDataKeys.MODULE.getData(dataContext);
		if(module == null) {
			return false;
		}

		if(ModuleUtilCore.getExtension(module, PhpModuleExtension.class) == null) {
			return false;
		}

		return true;
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
		builder.setTitle("Create New Class")
				.addKind("Class", PHPIcons2.Class, "PHP Class")
				.addKind("Interface", PHPIcons2.Interface, "PHP Interface");

	}

	@Override
	protected String getActionName(PsiDirectory psiDirectory, String s, String s2) {
		return "Creating class";
	}
}