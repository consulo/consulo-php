package org.consulo.php.lang.psi.resolve;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpParameter;
import org.consulo.php.lang.psi.PhpVariableReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Apr 15, 2008 10:04:22 AM
 */
public class PhpScopeProcessor implements PsiScopeProcessor
{
	protected PhpElement element;

	public PhpScopeProcessor(PhpElement element)
	{
		this.element = element;
	}

	public boolean execute(PsiElement element)
	{
		return false;
	}

	@Override
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

	@Override
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
