package org.consulo.php.lang.psi.elements.impl;

import org.consulo.php.lang.psi.elements.AssignmentExpression;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:27 AM
 */
public class AssignmentExpressionImpl extends PhpTypedElementImpl implements AssignmentExpression
{
	public AssignmentExpressionImpl(ASTNode node)
	{
		super(node);
	}

	public PsiElement getVariable()
	{
		return getFirstChild();
	}

	public PsiElement getValue()
	{
		return getLastChild();
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) psiElementVisitor).visitPhpAssignmentExpression(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(getValue() != lastParent && getVariable() != lastParent)
		{
			if(!getValue().processDeclarations(processor, resolveState, null, source))
			{
				return false;
			}
			if(!getVariable().processDeclarations(processor, resolveState, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, resolveState, lastParent, source);
	}

}
