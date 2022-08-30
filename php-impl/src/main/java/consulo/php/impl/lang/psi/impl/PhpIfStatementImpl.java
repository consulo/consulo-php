package consulo.php.impl.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.PhpElseIfStatement;
import consulo.php.impl.lang.psi.PhpElseStatement;
import consulo.php.impl.lang.psi.PhpIfStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;

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
	public PhpPsiElement getCondition()
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
	public PhpPsiElement getStatement()
	{
		if(getCondition() != null)
		{
			return getCondition().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitIfStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
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
		else if(lastParent instanceof PhpPsiElement)
		{
			PhpPsiElement statement = ((PhpPsiElement) lastParent).getPrevPsiSibling();
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
