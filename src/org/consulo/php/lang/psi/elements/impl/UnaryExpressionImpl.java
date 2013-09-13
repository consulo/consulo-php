package org.consulo.php.lang.psi.elements.impl;

import org.consulo.php.lang.psi.elements.UnaryExpression;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 9:10:04 PM
 */
public class UnaryExpressionImpl extends PhpElementImpl implements UnaryExpression
{
	public UnaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement getExpression()
	{
		return getFirstPsiChild();
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) visitor).visitPhpUnaryExpression(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(getExpression() != lastParent)
		{
			if(!getExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
