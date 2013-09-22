package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpElseIfStatement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:57 AM
 */
public class PhpElseIfStatementImpl extends PhpElementImpl implements PhpElseIfStatement
{

	public PhpElseIfStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpElement getCondition()
	{
		return getFirstPsiChild();
	}

	@Override
	public PhpElement getStatement()
	{
		if(getCondition() != null)
		{
			return getCondition().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitElseIfStatement(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(getCondition() != lastParent)
		{
			if(!getCondition().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getStatement() != lastParent)
			{
				if(!getStatement().processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
