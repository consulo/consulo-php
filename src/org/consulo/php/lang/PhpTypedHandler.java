package org.consulo.php.lang;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.consulo.php.completion.PhpCompletionUtil;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.impl.PhpFileImpl;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
public class PhpTypedHandler extends TypedHandlerDelegate
{
	@Override
	public Result checkAutoPopup(char c, Project project, Editor editor, PsiFile psiFile)
	{
		if(!(psiFile instanceof PhpFileImpl))
			return Result.CONTINUE;
		if(c == '$')
		{
			PhpCompletionUtil.showCompletion(editor);
		}
		if(c == '>')
		{
			int offset = editor.getCaretModel().getOffset();
			PsiElement psiElement = psiFile.findElementAt(offset - 1);
			//noinspection ConstantConditions
			if(psiElement != null && psiElement.getNode().getElementType() == PhpTokenTypes.opMINUS)
			{
				PhpCompletionUtil.showCompletion(editor);
			}
		}
		return Result.STOP;
	}
}