package net.jay.plugins.php.refactoring;

import org.jetbrains.annotations.Nullable;
import com.intellij.lang.refactoring.InlineHandler;
import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;

/**
 * @author jay
 * @date Jul 1, 2008 2:52:51 PM
 */
public class PhpRefactoringSupportProvider implements RefactoringSupportProvider
{

	public boolean isSafeDeleteAvailable(PsiElement element)
	{
		return false;
	}

	@Nullable
	public RefactoringActionHandler getIntroduceVariableHandler()
	{
		return null;
	}

	@Nullable
	public RefactoringActionHandler getExtractMethodHandler()
	{
		return null;
	}

	public RefactoringActionHandler getIntroduceConstantHandler()
	{
		return null;
	}

	public RefactoringActionHandler getIntroduceFieldHandler()
	{
		return null;
	}

	public boolean doInplaceRenameFor(PsiElement element, PsiElement context)
	{
		return false;
	}

	@Nullable
	public InlineHandler getInlineHandler()
	{
		return null;
	}

}
