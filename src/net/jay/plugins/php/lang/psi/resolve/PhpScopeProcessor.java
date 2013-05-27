package net.jay.plugins.php.lang.psi.resolve;

import net.jay.plugins.php.lang.psi.elements.Parameter;
import net.jay.plugins.php.lang.psi.elements.Variable;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 10:04:22 AM
 */
public class PhpScopeProcessor implements PsiScopeProcessor
{
	protected PsiNamedElement element;

	public PhpScopeProcessor(PsiNamedElement element)
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

	public <T> T getHint(Class<T> tClass)
	{
		return null;
	}

	public void handleEvent(Event event, Object o)
	{
	}

	protected boolean isAppropriateDeclarationType(PsiElement possibleDeclaration)
	{
		if(element instanceof Variable)
		{
			return possibleDeclaration instanceof Variable || possibleDeclaration instanceof Parameter;
		}
		return false;
	}
}
