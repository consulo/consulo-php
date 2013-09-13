package org.consulo.php.lang.psi.elements.impl;

import net.jay.plugins.php.lang.psi.elements.For;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:23:09 AM
 */
public class ForImpl extends PHPPsiElementImpl implements For
{

	public ForImpl(ASTNode node)
	{
		super(node);
	}

	public PHPPsiElement getInitialExpression()
	{
		return getFirstPsiChild();
	}

	public PHPPsiElement getConditionalExpression()
	{
		final PHPPsiElement expression = getInitialExpression();
		if(expression != null)
			return getInitialExpression().getNextPsiSibling();
		return null;
	}

	public PHPPsiElement getRepeatedExpression()
	{
		final PHPPsiElement expression = getConditionalExpression();
		if(expression != null)
			return getConditionalExpression().getNextPsiSibling();
		return null;
	}

	public PHPPsiElement getStatement()
	{
		final PHPPsiElement expression = getRepeatedExpression();
		if(expression != null)
		{
			return getRepeatedExpression().getNextPsiSibling();
		}
		return null;
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpFor(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(lastParent == null)
		{
			if(getStatement() != null && !getStatement().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getRepeatedExpression() != null && !getRepeatedExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getConditionalExpression() != null && !getConditionalExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getInitialExpression() != null && !getInitialExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		else if(lastParent instanceof PHPPsiElement)
		{
			PHPPsiElement statement = ((PHPPsiElement) lastParent).getPrevPsiSibling();
			while(statement != null)
			{
				if(!statement.processDeclarations(processor, state, null, source))
				{
					return false;
				}
				statement = statement.getPrevPsiSibling();
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
