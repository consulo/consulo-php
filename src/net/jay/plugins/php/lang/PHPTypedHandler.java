package net.jay.plugins.php.lang;

import net.jay.plugins.php.completion.PhpCompletionUtil;
import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.PHPFile;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
public class PHPTypedHandler extends TypedHandlerDelegate
{
	@Override
	public Result checkAutoPopup(char c, Project project, Editor editor, PsiFile psiFile)
	{
		if(!(psiFile instanceof PHPFile))
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
			if(psiElement != null && psiElement.getNode().getElementType() == PHPTokenTypes.opMINUS)
			{
				PhpCompletionUtil.showCompletion(editor);
			}
		}
		return Result.STOP;
	}
}