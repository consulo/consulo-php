package net.jay.plugins.php.refactoring;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jul 1, 2008 2:52:51 PM
 */
public class PhpRefactoringSupportProvider extends RefactoringSupportProvider
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

}
