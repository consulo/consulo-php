package org.consulo.php.refactoring;

import org.jetbrains.annotations.Nullable;
import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;

/**
 * @author jay
 * @date Jul 1, 2008 2:52:51 PM
 */
public class PhpRefactoringSupportProvider extends RefactoringSupportProvider
{

	@Override
	public boolean isSafeDeleteAvailable(PsiElement element)
	{
		return false;
	}

	@Override
	@Nullable
	public RefactoringActionHandler getIntroduceVariableHandler()
	{
		return null;
	}

	@Override
	@Nullable
	public RefactoringActionHandler getExtractMethodHandler()
	{
		return null;
	}

	@Override
	public RefactoringActionHandler getIntroduceConstantHandler()
	{
		return null;
	}

	@Override
	public RefactoringActionHandler getIntroduceFieldHandler()
	{
		return null;
	}

}
