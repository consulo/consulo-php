package consulo.php.impl.actions;

import consulo.annotation.access.RequiredReadAction;
import consulo.application.AllIcons;
import consulo.dataContext.DataContext;
import consulo.fileEditor.FileEditorManager;
import consulo.fileTemplate.FileTemplate;
import consulo.fileTemplate.FileTemplateManager;
import consulo.fileTemplate.FileTemplateParseException;
import consulo.fileTemplate.FileTemplateUtil;
import consulo.ide.IdeView;
import consulo.ide.action.CreateFileAction;
import consulo.ide.action.CreateFileFromTemplateAction;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.language.editor.LangDataKeys;
import consulo.language.editor.PlatformDataKeys;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.util.IncorrectOperationException;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.php.PhpLanguageLevel;
import consulo.php.icon.PhpIconGroup;
import consulo.php.impl.lang.psi.PhpPackage;
import consulo.php.impl.lang.psi.util.PhpPsiUtil;
import consulo.php.module.extension.PhpModuleExtension;
import consulo.php.module.util.PhpModuleExtensionUtil;
import consulo.project.Project;
import consulo.project.ProjectPropertiesComponent;
import consulo.ui.ex.awt.Messages;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Map;

/**
 * @author VISTALL
 * @since 05.07.13.
 */
public class PhpCreateClassAction extends CreateFileFromTemplateAction
{
	public PhpCreateClassAction()
	{
		super("Php Class", "Create new Php Class", PhpIconGroup.filetypesPhp());
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
					ProjectPropertiesComponent.getInstance(project).setValue(defaultTemplateProperty, template.getName());
				}
				return psiFile;
			}
		}
		catch(FileTemplateParseException e)
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
	@RequiredReadAction
	protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder.setTitle("Create New Class").addKind("Class", AllIcons.Nodes.Class, "PHP Class").addKind("Interface", AllIcons.Nodes.Interface, "PHP Interface");

		if(PhpModuleExtensionUtil.getLanguageLevel(psiDirectory).isAtLeast(PhpLanguageLevel.PHP_5_4))
		{
			builder.addKind("Trait", AllIcons.Nodes.Trait, "Php Trait");
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
