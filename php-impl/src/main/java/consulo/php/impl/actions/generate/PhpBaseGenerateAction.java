package consulo.php.impl.actions.generate;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.codeEditor.Editor;
import consulo.language.editor.action.CodeInsightAction;
import consulo.language.editor.action.CodeInsightActionHandler;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;
import consulo.project.Project;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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
