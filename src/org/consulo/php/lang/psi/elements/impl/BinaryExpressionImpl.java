package org.consulo.php.lang.psi.elements.impl;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.psi.elements.BinaryExpression;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @date Apr 4, 2008 10:55:12 AM
 */
public class BinaryExpressionImpl extends PHPPsiElementImpl implements BinaryExpression
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
		return getNode().getChildren(PHPTokenTypes.tsOPERATORS)[0].getElementType();
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpBinaryExpression(this);
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
