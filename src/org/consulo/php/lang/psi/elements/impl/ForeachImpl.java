package org.consulo.php.lang.psi.elements.impl;

import org.consulo.php.lang.psi.elements.Foreach;
import org.consulo.php.lang.psi.elements.PhpVariableReference;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 3:36:30 PM
 */
public class ForeachImpl extends PHPPsiElementImpl implements Foreach
{
	public ForeachImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement getArray()
	{
		return getFirstPsiChild();
	}

	public PhpVariableReference getKey()
	{
		PsiElement[] children = getChildren();
		int variables = 0;
		PhpVariableReference firstVariable = null;
		for(int i = 1; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child instanceof PhpVariableReference)
			{
				if(++variables == 1)
				{
					firstVariable = (PhpVariableReference) child;
				}
			}
		}
		if(variables == 2)
		{
			return firstVariable;
		}
		return null;
	}

	public PhpVariableReference getValue()
	{
		PsiElement lastChild = getLastChild();
		while(lastChild != null)
		{
			if(lastChild instanceof PhpVariableReference)
			{
				return (PhpVariableReference) lastChild;
			}
			lastChild = lastChild.getPrevSibling();
		}
		return null;
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpForeach(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(getKey() != null && lastParent != getArray() && !getKey().processDeclarations(processor, state, null, source))
		{
			return false;
		}
		if(getValue() != null && lastParent != getArray() && !getValue().processDeclarations(processor, state, null, source))
		{
			return false;
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
