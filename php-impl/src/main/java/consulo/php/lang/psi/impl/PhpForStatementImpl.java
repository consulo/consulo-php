package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.psi.PhpForStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:23:09 AM
 */
public class PhpForStatementImpl extends PhpElementImpl implements PhpForStatement
{

	public PhpForStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpPsiElement getInitialExpression()
	{
		return getFirstPsiChild();
	}

	@Override
	public PhpPsiElement getConditionalExpression()
	{
		final PhpPsiElement expression = getInitialExpression();
		if(expression != null)
		{
			return getInitialExpression().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public PhpPsiElement getRepeatedExpression()
	{
		final PhpPsiElement expression = getConditionalExpression();
		if(expression != null)
		{
			return getConditionalExpression().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public PhpPsiElement getStatement()
	{
		final PhpPsiElement expression = getRepeatedExpression();
		if(expression != null)
		{
			return getRepeatedExpression().getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitForStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(lastParent == null)
		{
			if(getStatement() != null && !getStatement().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getRepeatedExpression() != null && !getRepeatedExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getConditionalExpression() != null && !getConditionalExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getInitialExpression() != null && !getInitialExpression().processDeclarations(processor, state, null, source))
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
