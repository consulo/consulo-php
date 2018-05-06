package consulo.php.actions.generate;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.impl.PhpFileImpl;

import javax.annotation.Nullable;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author VISTALL
 * @since 04.07.13.
 */
public abstract class PhpBaseGenerateAction extends CodeInsightAction
{
	private final CodeInsightActionHandler myHandler;

	public PhpBaseGenerateAction(CodeInsightActionHandler handler)
	{
		myHandler = handler;
	}

	@Nonnull
	@Override
	protected final CodeInsightActionHandler getHandler()
	{
		return myHandler;
	}

	@Nullable
	protected PhpClass getTargetClass(Editor editor, PsiFile file)
	{
		int offset = editor.getCaretModel().getOffset();
		PsiElement element = file.findElementAt(offset);
		if(element == null)
		{
			return null;
		}
		final PhpClass target = PsiTreeUtil.getParentOfType(element, PhpClass.class);
		return target;
	}

	@Override
	protected boolean isValidForFile(@Nonnull Project project, @Nonnull Editor editor, @Nonnull PsiFile file)
	{
		if(!(file instanceof PhpFileImpl))
		{
			return false;
		}

		PsiDocumentManager.getInstance(project).commitAllDocuments();

		PhpClass targetClass = getTargetClass(editor, file);
		return targetClass != null && isValidForClass(targetClass);
	}

	protected boolean isValidForClass(final PhpClass targetClass)
	{
		return !targetClass.isInterface();
	}
}
