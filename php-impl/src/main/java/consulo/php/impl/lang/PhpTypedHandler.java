package consulo.php.impl.lang;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.php.impl.completion.PhpCompletionUtil;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;
import consulo.language.editor.action.TypedHandlerDelegate;
import consulo.codeEditor.Editor;
import consulo.project.Project;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
@ExtensionImpl
public class PhpTypedHandler extends TypedHandlerDelegate
{
	@Override
	public Result checkAutoPopup(char c, Project project, Editor editor, PsiFile psiFile)
	{
		if(!(psiFile instanceof PhpFileImpl))
		{
			return Result.CONTINUE;
		}
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