package consulo.php.impl.completion.insert;

import com.jetbrains.php.lang.psi.elements.Function;
import consulo.codeEditor.Editor;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.psi.PsiElement;
import consulo.php.impl.lang.documentation.params.PhpParameterInfoHandlerUtil;

/**
 * @author jay
 * @date Jun 24, 2008 4:51:54 PM
 */
public class PhpMethodInsertHandler implements InsertHandler<LookupElement>
{
	private static final PhpMethodInsertHandler instance = new PhpMethodInsertHandler();

	public static PhpMethodInsertHandler getInstance()
	{
		return instance;
	}

	@Override
	public void handleInsert(InsertionContext context, LookupElement lookupElement)
	{
		Editor editor = context.getEditor();

		final Function method = getMethod(editor, lookupElement);
		if(!PhpInsertHandlerUtil.isStringAtCaret(editor, "("))
		{
			PhpInsertHandlerUtil.insertStringAtCaret(editor, "()");
			if(method != null)
			{
				if(method.getParameters().length > 0)
				{
					editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
					PhpParameterInfoHandlerUtil.showParameterInfo(editor);
				}
			}
		}
		else
		{
			if(PhpInsertHandlerUtil.isStringAtCaret(editor, "()"))
			{
				editor.getCaretModel().moveCaretRelatively(2, 0, false, false, true);
			}
			else
			{
				editor.getCaretModel().moveCaretRelatively(1, 0, false, false, true);
				PhpParameterInfoHandlerUtil.showParameterInfo(editor);
			}
		}
	}

	protected Function getMethod(Editor editor, LookupElement element)
	{
		final PsiElement psiElement = element.getPsiElement();
		if(psiElement instanceof Function)
		{
			return (Function) psiElement;
		}
		return null;
	}

}
