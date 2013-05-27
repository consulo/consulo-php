package net.jay.plugins.php.actions.generate;

import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.PhpClass;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Jul 1, 2008 2:42:01 AM
 */
public class PhpGenerateGetterAction extends BaseGenerateAction
{
	public PhpGenerateGetterAction()
	{
		super(new PhpGenerateGetterActionHandler());
	}

	protected boolean isValidForFile(Project project, Editor editor, PsiFile file)
	{
		if(!(file instanceof PHPFile))
			return false;

		PsiDocumentManager.getInstance(project).commitAllDocuments();

		//noinspection ConstantConditions
		final PhpClass phpClass = PsiTreeUtil.getChildOfType(file.getFirstChild(), PhpClass.class);
		return phpClass != null;
	}
}
