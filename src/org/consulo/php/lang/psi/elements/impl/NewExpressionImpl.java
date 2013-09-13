package org.consulo.php.lang.psi.elements.impl;

import org.consulo.php.lang.psi.elements.NewExpression;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 1:34:34 AM
 */
public class NewExpressionImpl extends PhpTypedElementImpl implements NewExpression
{
	public NewExpressionImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) psiElementVisitor).visitPhpNewExpression(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}
}
