package consulo.php.actions;

import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateFileAction;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.PhpIcons;
import consulo.php.PhpLanguageLevel;
import consulo.php.lang.psi.PhpPackage;
import consulo.php.lang.psi.util.PhpPsiUtil;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.module.util.PhpModuleExtensionUtil;
import org.apache.velocity.runtime.parser.ParseException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author VISTALL
 * @since 05.07.13.
 */
public class PhpCreateClassAction extends CreateFileFromTemplateAction
{
	public PhpCreateClassAction()
	{
		super("Php Class", "Create new Php Class", PhpIcons.Php);
	}

	@SuppressWarnings("DialogTitleCapitalization")
	@Nullable
	@RequiredReadAction
	public static PsiFile createFileFromTemplate(@Nullable String name, @Nonnull FileTemplate template, @Nonnull PsiDirectory dir, @Nullable String defaultTemplateProperty)
	{
		CreateFileAction.MkDirs mkdirs = new CreateFileAction.MkDirs(name, dir);
		name = mkdirs.newName;
		dir = mkdirs.directory;
		PsiElement element;
		Project project = dir.getProject();
		try
		{
			Map<String, Object> defaultProperties = FileTemplateManager.getInstance(project).getDefaultVariables();

			if(PhpModuleExtensionUtil.getLanguageLevel(dir).isAtLeast(PhpLanguageLevel.PHP_5_3))
			{
				PhpPackage psiPackage = PhpPsiUtil.findPackage(project, dir);

				if(psiPackage != null)
				{
					defaultProperties.put("NAMESPACE", psiPackage.getNamespaceName());
				}
			}

			element = FileTemplateUtil.createFromTemplate(template, name, defaultProperties, dir);
			final PsiFile psiFile = element.getContainingFile();

			final VirtualFile virtualFile = psiFile.getVirtualFile();
			if(virtualFile != null)
			{
				FileEditorManager.getInstance(project).openFile(virtualFile, true);
				if(defaultTemplateProperty != null)
				{
					PropertiesComponent.getInstance(project).setValue(defaultTemplateProperty, template.getName());
				}
				return psiFile;
			}
		}
		catch(ParseException e)
		{
			Messages.showErrorDialog(project, "Error parsing Velocity template: " + e.getMessage(), "Create File from Template");
			return null;
		}
		catch(IncorrectOperationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			LOG.error(e);
		}

		return null;
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
	@RequiredReadAction
	protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder.setTitle("Create New Class").addKind("Class", PhpIcons.Class, "PHP Class").addKind("Interface", PhpIcons.Interface, "PHP Interface");

		if(PhpModuleExtensionUtil.getLanguageLevel(psiDirectory).isAtLeast(PhpLanguageLevel.PHP_5_4))
		{
			builder.addKind("Trait", PhpIcons.Trait, "Php Trait");
		}
	}

	@Override
	protected PsiFile createFile(String name, String templateName, PsiDirectory dir)
	{
		final FileTemplate template = FileTemplateManager.getInstance(dir.getProject()).getInternalTemplate(templateName);
		return createFileFromTemplate(name, template, dir);
	}

	@Override
	protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir)
	{
		return createFileFromTemplate(name, template, dir, getDefaultTemplateProperty());
	}

	@Override
	protected String getActionName(PsiDirectory psiDirectory, String s, String s2)
	{
		return "Creating class";
	}
}
