package org.consulo.php.completion.insert;

import org.consulo.php.completion.PhpCompletionUtil;
import org.consulo.php.completion.PhpLookupItem;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.TemplateInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;

/**
 * @author jay
 * @date Jun 30, 2008 4:24:14 PM
 */
public class PhpClassInsertHandler extends TemplateInsertHandler
{

	private static PhpClassInsertHandler instance = null;

	@Override
	public void handleInsert(InsertionContext context, LookupElement lookupElement)
	{
		super.handleInsert(context, lookupElement);
		if(lookupElement.getObject() instanceof PhpLookupItem)
		{
			if(!PhpInsertHandlerUtil.isStringAtCaret(context.getEditor(), "::"))
			{
				PhpInsertHandlerUtil.insertStringAtCaret(context.getEditor(), "::");
				PhpCompletionUtil.showCompletion(context);
			}
		}
	}

	public static PhpClassInsertHandler getInstance()
	{
		if(instance == null)
		{
			instance = new PhpClassInsertHandler();
		}
		return instance;
	}

	private PhpClassInsertHandler()
	{

	}

}
