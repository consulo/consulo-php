package net.jay.plugins.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.lang.psi.elements.Function;
import net.jay.plugins.php.lang.psi.elements.PhpParameter;
import net.jay.plugins.php.lang.psi.elements.PhpParameterList;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author jay
 * @date Apr 3, 2008 10:16:23 PM
 */
public class FunctionImpl extends PhpNamedElementImpl implements Function
{
	public FunctionImpl(ASTNode node)
	{
		super(node);
	}

	@NotNull
	public PhpParameter[] getParameters()
	{
		PhpParameterList parameterList = getParameterList();
		if(parameterList == null) {
			return PhpParameter.EMPTY_ARRAY;
		}
		return parameterList.getParameters();
	}

	public PhpParameterList getParameterList()
	{
		return PsiTreeUtil.getChildOfType(this, PhpParameterList.class);
	}

	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return null;
	}

	public void accept(@NotNull final PsiElementVisitor psiElementVisitor)
	{
		if(psiElementVisitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) psiElementVisitor).visitPhpFunction(this);
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
		return PHPIcons.FIELD;
	}
}
