package org.consulo.php.completion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Editor;

/**
 * @author jay
 * @date Jun 30, 2008 8:22:55 PM
 */
public class PhpCompletionUtil
{

	public static void invokeCompletion(Editor editor)
	{
		((CodeInsightAction) ActionManager.getInstance().getAction(IdeActions.ACTION_CODE_COMPLETION)).actionPerformedImpl(editor.getProject(), editor);
	}

	public static void showCompletion(InsertionContext context)
	{
		AutoPopupController.getInstance(context.getProject()).autoPopupMemberLookup(context.getEditor(), null);
	}

	public static void showCompletion(Editor editor)
	{
		AutoPopupController.getInstance(editor.getProject()).autoPopupMemberLookup(editor, null);
	}
}
