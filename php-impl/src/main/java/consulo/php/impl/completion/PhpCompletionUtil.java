package consulo.php.impl.completion;

import consulo.language.editor.AutoPopupController;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.IdeActions;
import consulo.codeEditor.Editor;
import consulo.language.editor.action.CodeInsightAction;
import consulo.language.editor.completion.lookup.InsertionContext;

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
