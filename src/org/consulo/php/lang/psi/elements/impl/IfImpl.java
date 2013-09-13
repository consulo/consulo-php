package org.consulo.php.lang.psi.elements.impl;

import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.lang.psi.elements.Else;
import net.jay.plugins.php.lang.psi.elements.ElseIf;
import net.jay.plugins.php.lang.psi.elements.If;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date May 9, 2008 3:48:51 PM
 */
public class IfImpl extends PHPPsiElementImpl implements If
{

	public IfImpl(ASTNode node)
	{
		super(node);
	}

	public PHPPsiElement getCondition()
	{
		return getFirstPsiChild();
	}

	public ElseIf[] getElseIfBranches()
	{
		List<ElseIf> result = new ArrayList<ElseIf>();
		for(PsiElement element : getChildren())
		{
			if(element instanceof ElseIf)
			{
				result.add((ElseIf) element);
			}
		}
		return result.toArray(new ElseIf[result.size()]);
	}

	public Else getElseBranch()
	{
		return PsiTreeUtil.getChildOfType(this, Else.class);
	}

	@SuppressWarnings({"ConstantConditions"})
	public PHPPsiElement getStatement()
	{
		if(getCondition() != null)
			return getCondition().getNextPsiSibling();
		return null;
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpIf(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(lastParent == null)
		{
			if(getElseBranch() != null && !getElseBranch().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			for(ElseIf elseIf : getElseIfBranches())
			{
				if(!elseIf.processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
			if(getStatement() != null && !getStatement().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getCondition() != null && !getCondition().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		else if(lastParent instanceof PHPPsiElement)
		{
			PHPPsiElement statement = ((PHPPsiElement) lastParent).getPrevPsiSibling();
			while(statement != null)
			{
				if(!statement.processDeclarations(processor, state, null, source))
				{
					return false;
				}
				statement = statement.getPrevPsiSibling();
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
