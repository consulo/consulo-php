package org.consulo.php.lang.psi.resolve;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.consulo.php.lang.psi.elements.PHPPsiElement;
import org.consulo.php.lang.psi.elements.PhpParameter;
import org.consulo.php.lang.psi.elements.PhpVariableReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Apr 15, 2008 10:04:22 AM
 */
public class PhpScopeProcessor implements PsiScopeProcessor
{
	protected PHPPsiElement element;

	public PhpScopeProcessor(PHPPsiElement element)
	{
		this.element = element;
	}

	public boolean execute(PsiElement element)
	{
		return false;
	}

	public boolean execute(PsiElement element, ResolveState state)
	{
		return execute(element);
	}

	@Nullable
	@Override
	public <T> T getHint(@NotNull Key<T> tKey)
	{
		return null;
	}

	public void handleEvent(Event event, Object o)
	{
	}

	protected boolean isAppropriateDeclarationType(PsiElement possibleDeclaration)
	{
		if(element instanceof PhpVariableReference)
		{
			return possibleDeclaration instanceof PhpVariableReference || possibleDeclaration instanceof PhpParameter;
		}
		return false;
	}
}
