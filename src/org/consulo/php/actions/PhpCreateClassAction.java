package org.consulo.php.actions;

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
import org.apache.velocity.runtime.parser.ParseException;
import org.consulo.php.PhpIcons2;
import org.consulo.php.PhpLanguageLevel;
import org.consulo.php.lang.psi.PhpPackage;
import org.consulo.php.lang.psi.util.PhpPsiUtil;
import org.consulo.php.module.extension.PhpModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * @author VISTALL
 * @since 05.07.13.
 */
public class PhpCreateClassAction extends CreateFileFromTemplateAction {
	public PhpCreateClassAction() {
		super("Php Class", "Create new Php Class", PhpIcons2.Php);
	}

	@SuppressWarnings("DialogTitleCapitalization")
	@Nullable
	public static com.intellij.psi.PsiFile createFileFromTemplate(@Nullable String name,
																  @NotNull FileTemplate template,
																  @NotNull PsiDirectory dir,
																  @Nullable String defaultTemplateProperty) {
		CreateFileAction.MkDirs mkdirs = new CreateFileAction.MkDirs(name, dir);
		name = mkdirs.newName;
		dir = mkdirs.directory;
		PsiElement element;
		Project project = dir.getProject();
		try {
			Properties defaultProperties = FileTemplateManager.getInstance().getDefaultProperties(project);

			PhpModuleExtension phpModuleExtension = getPhpModuleExtension(dir);
			if(phpModuleExtension.getLanguageLevel().isAtLeast(PhpLanguageLevel.PHP_5_3)) {
				PhpPackage psiPackage = PhpPsiUtil.findPackage(project, dir);

				assert psiPackage != null;

				defaultProperties.put("NAMESPACE", psiPackage.getNamespaceName());
			}

			element = FileTemplateUtil
					.createFromTemplate(template, name, defaultProperties, dir);
			final PsiFile psiFile = element.getContainingFile();

			final VirtualFile virtualFile = psiFile.getVirtualFile();
			if (virtualFile != null) {
				FileEditorManager.getInstance(project).openFile(virtualFile, true);
				if (defaultTemplateProperty != null) {
					PropertiesComponent.getInstance(project).setValue(defaultTemplateProperty, template.getName());
				}
				return psiFile;
			}
		} catch (ParseException e) {
			Messages.showErrorDialog(project, "Error parsing Velocity template: " + e.getMessage(), "Create File from Template");
			return null;
		} catch (IncorrectOperationException e) {
			throw e;
		} catch (Exception e) {
			LOG.error(e);
		}

		return null;
	}

	@Override
	protected boolean isAvailable(final DataContext dataContext) {
		final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
		final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
		if (project == null || view == null || view.getDirectories().length == 0) {
			return false;
		}

		final Module module = LangDataKeys.MODULE.getData(dataContext);
		if (module == null) {
			return false;
		}

		if (ModuleUtilCore.getExtension(module, PhpModuleExtension.class) == null) {
			return false;
		}

		return true;
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
		builder.setTitle("Create New Class")
				.addKind("Class", PhpIcons2.Class, "PHP Class")
				.addKind("Interface", PhpIcons2.Interface, "PHP Interface");

		PhpModuleExtension extension = getPhpModuleExtension(psiDirectory);

		if (extension.getLanguageLevel().isAtLeast(PhpLanguageLevel.PHP_5_4)) {
			builder.addKind("Trait", PhpIcons2.Trait, "Php Trait");
		}
	}

	private static PhpModuleExtension getPhpModuleExtension(PsiDirectory psiDirectory) {
		Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(psiDirectory);

		assert moduleForPsiElement != null;

		PhpModuleExtension extension = ModuleUtilCore.getExtension(moduleForPsiElement, PhpModuleExtension.class);

		assert extension != null;
		return extension;
	}

	@Override
	protected PsiFile createFile(String name, String templateName, PsiDirectory dir) {
		final FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(templateName);
		return createFileFromTemplate(name, template, dir);
	}

	@Override
	protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir) {
		return createFileFromTemplate(name, template, dir, getDefaultTemplateProperty());
	}

	@Override
	protected String getActionName(PsiDirectory psiDirectory, String s, String s2) {
		return "Creating class";
	}
}
