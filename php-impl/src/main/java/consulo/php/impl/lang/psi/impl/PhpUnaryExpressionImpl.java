package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.PhpUnaryExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 9:10:04 PM
 */
public class PhpUnaryExpressionImpl extends PhpElementImpl implements PhpUnaryExpression
{
	public PhpUnaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getExpression()
	{
		return getFirstPsiChild();
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUnaryExpression(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(getExpression() != lastParent)
		{
			if(!getExpression().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
