package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpForStatement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:23:09 AM
 */
public class PhpForStatementImpl extends PhpElementImpl implements PhpForStatement
{

	public PhpForStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpElement getInitialExpression()
	{
		return getFirstPsiChild();
	}

	@Override
	public PhpElement getConditionalExpression()
	{
		final PhpElement expression = getInitialExpression();
		if(expression != null)
		{
			return getInitialExpression().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public PhpElement getRepeatedExpression()
	{
		final PhpElement expression = getConditionalExpression();
		if(expression != null)
		{
			return getConditionalExpression().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public PhpElement getStatement()
	{
		final PhpElement expression = getRepeatedExpression();
		if(expression != null)
		{
			return getRepeatedExpression().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitForStatement(this);
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
		else if(lastParent instanceof PhpElement)
		{
			PhpElement statement = ((PhpElement) lastParent).getPrevPsiSibling();
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
