package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpAssignmentExpression;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:27 AM
 */
public class PhpAssignmentExpressionImpl extends PhpTypeOwnerImpl implements PhpAssignmentExpression
{
	public PhpAssignmentExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getVariable()
	{
		return getFirstChild();
	}

	@Override
	public PsiElement getValue()
	{
		return getLastChild();
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitAssignmentExpression(this);
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
