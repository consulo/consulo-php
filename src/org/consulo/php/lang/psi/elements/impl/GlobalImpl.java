package org.consulo.php.lang.psi.elements.impl;

import java.util.ArrayList;
import java.util.List;

import org.consulo.php.lang.psi.elements.Global;
import org.consulo.php.lang.psi.elements.PhpVariableReference;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date May 5, 2008 8:06:55 AM
 */
public class GlobalImpl extends PhpElementImpl implements Global
{
	public GlobalImpl(ASTNode node)
	{
		super(node);
	}

	public PhpVariableReference[] getVariables()
	{
		List<PhpVariableReference> variables = new ArrayList<PhpVariableReference>();
		for(PsiElement element : this.getChildren())
		{
			if(element instanceof PhpVariableReference)
			{
				variables.add((PhpVariableReference) element);
			}
		}
		return variables.toArray(new PhpVariableReference[variables.size()]);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		for(PhpVariableReference variable : this.getVariables())
		{
			if(!processor.execute(variable, state))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
