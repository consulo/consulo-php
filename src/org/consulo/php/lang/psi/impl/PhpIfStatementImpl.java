package org.consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpElseIfStatement;
import org.consulo.php.lang.psi.PhpElseStatement;
import org.consulo.php.lang.psi.PhpIfStatement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date May 9, 2008 3:48:51 PM
 */
public class PhpIfStatementImpl extends PhpElementImpl implements PhpIfStatement
{

	public PhpIfStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpElement getCondition()
	{
		return getFirstPsiChild();
	}

	@Override
	public PhpElseIfStatement[] getElseIfBranches()
	{
		List<PhpElseIfStatement> result = new ArrayList<PhpElseIfStatement>();
		for(PsiElement element : getChildren())
		{
			if(element instanceof PhpElseIfStatement)
			{
				result.add((PhpElseIfStatement) element);
			}
		}
		return result.toArray(new PhpElseIfStatement[result.size()]);
	}

	@Override
	public PhpElseStatement getElseBranch()
	{
		return PsiTreeUtil.getChildOfType(this, PhpElseStatement.class);
	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public PhpElement getStatement()
	{
		if(getCondition() != null)
		{
			return getCondition().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitIfStatement(this);
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
			for(PhpElseIfStatement elseIf : getElseIfBranches())
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
		else if(lastParent instanceof PhpElement)
		{
			PhpElement statement = ((PhpElement) lastParent).getPrevPsiSibling();
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
