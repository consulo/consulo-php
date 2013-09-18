package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.consulo.php.PhpIcons;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpParameter;
import org.consulo.php.lang.psi.PhpParameterList;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jay
 * @date Apr 3, 2008 10:16:23 PM
 */
public class PhpFunctionImpl extends PhpNamedElementImpl implements PhpFunction
{
	public PhpFunctionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	@NotNull
	public PhpParameter[] getParameters()
	{
		PhpParameterList parameterList = getParameterList();
		if(parameterList == null) {
			return PhpParameter.EMPTY_ARRAY;
		}
		return parameterList.getParameters();
	}

	@Override
	public PhpParameterList getParameterList()
	{
		return PsiTreeUtil.getChildOfType(this, PhpParameterList.class);
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return null;
	}

	@Override
	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) psiElementVisitor).visitPhpFunction(this);
		}
		else
		{
			super.accept(psiElementVisitor);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement psiElement, @NotNull PsiElement psiElement1)
	{
		for(PhpParameter parameter : getParameters())
		{
			if(!processor.execute(parameter, resolveState))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, resolveState, psiElement, psiElement1);
	}

	@Override
	public Icon getIcon()
	{
		return PhpIcons.FIELD;
	}
}
