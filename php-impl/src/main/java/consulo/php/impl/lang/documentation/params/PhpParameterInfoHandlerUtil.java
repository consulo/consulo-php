package consulo.php.impl.lang.documentation.params;

import consulo.codeEditor.Editor;
import consulo.language.editor.AutoPopupController;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;

/**
 * @author jay
 * @date Jun 30, 2008 8:20:42 PM
 */
public class PhpParameterInfoHandlerUtil
{
	public static void showParameterInfo(Editor editor)
	{
		final PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
		AutoPopupController.getInstance(editor.getProject()).showParameterInfo(editor.getProject(), editor, psiFile, -1, null, true);
	}
}
