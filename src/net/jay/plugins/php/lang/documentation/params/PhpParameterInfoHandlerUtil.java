package net.jay.plugins.php.lang.documentation.params;

import com.intellij.codeInsight.hint.ShowParameterInfoHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

/**
 * @author jay
 * @date Jun 30, 2008 8:20:42 PM
 */
public class PhpParameterInfoHandlerUtil
{

	public static void showParameterInfo(Editor editor)
	{
		final PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
		new ShowParameterInfoHandler().invoke(editor.getProject(), editor, psiFile);
	}

}
