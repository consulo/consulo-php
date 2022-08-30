package consulo.php.impl.completion.insert;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.codeEditor.Editor;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.psi.PsiElement;

/**
 * @author jay
 * @date Jun 24, 2008 7:34:13 PM
 */
public class PhpClassConstructorInsertHandler extends PhpMethodInsertHandler
{
	private static final PhpClassConstructorInsertHandler instance = new PhpClassConstructorInsertHandler();

	public static PhpClassConstructorInsertHandler getInstance()
	{
		return instance;
	}

	@Override
	protected Function getMethod(Editor editor, LookupElement element)
	{
		PsiElement psiElement = element.getPsiElement();
		if(psiElement instanceof PhpClass)
		{
			return ((PhpClass) psiElement).getConstructor();
		}
		return null;
	}
}
