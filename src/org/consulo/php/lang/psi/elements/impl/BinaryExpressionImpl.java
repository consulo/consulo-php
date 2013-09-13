package org.consulo.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.consulo.php.lang.psi.elements.BinaryExpression;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 4, 2008 10:55:12 AM
 */
public class BinaryExpressionImpl extends PhpElementImpl implements BinaryExpression
{
	public BinaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement getLeftOperand()
	{
		return getFirstPsiChild();
	}

	public PsiElement getRightOperand()
	{
		return getFirstPsiChild().getNextPsiSibling();
	}

	public IElementType getOperation()
	{
		return getNode().getChildren(PhpTokenTypes.tsOPERATORS)[0].getElementType();
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) visitor).visitPhpBinaryExpression(this);
		}
		else
		{
			visitor.visitElement(this);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(lastParent == null || lastParent == getRightOperand())
		{
			if(lastParent != getRightOperand())
			{
				if(!getRightOperand().processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
			if(!getLeftOperand().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
