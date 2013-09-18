package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpNewExpression;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 1:34:34 AM
 */
public class PhpNewExpressionImpl extends PhpTypedElementImpl implements PhpNewExpression
{
	public PhpNewExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) psiElementVisitor).visitPhpNewExpression(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}
}
