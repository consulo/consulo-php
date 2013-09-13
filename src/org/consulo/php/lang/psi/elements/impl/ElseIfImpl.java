package org.consulo.php.lang.psi.elements.impl;

import org.consulo.php.lang.psi.elements.ElseIf;
import org.consulo.php.lang.psi.elements.PhpElement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:57 AM
 */
public class ElseIfImpl extends PhpElementImpl implements ElseIf
{

	public ElseIfImpl(ASTNode node)
	{
		super(node);
	}

	public PhpElement getCondition()
	{
		return getFirstPsiChild();
	}

	public PhpElement getStatement()
	{
		if(getCondition() != null)
			return getCondition().getNextPsiSibling();
		return null;
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) visitor).visitPhpElseIf(this);
		}
		else
		{
			visitor.visitElement(this);
		}
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
