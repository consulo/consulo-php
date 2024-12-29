package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.impl.lang.psi.PhpElseIfStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:57 AM
 */
public class PhpElseIfStatementImpl extends PhpElementImpl implements PhpElseIfStatement
{

	public PhpElseIfStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpPsiElement getCondition()
	{
		return getFirstPsiChild();
	}

	@Override
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
		visitor.visitElseIfStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(getCondition() != lastParent)
		{
			if(!getCondition().processDeclarations(processor, state, null, source))
			{
				return false;
			}
			if(getStatement() != lastParent)
			{
				if(!getStatement().processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
