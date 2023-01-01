package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpAssignmentExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:27 AM
 */
public class PhpAssignmentExpressionImpl extends PhpTypedElementImpl implements PhpAssignmentExpression
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
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitAssignmentExpression(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState resolveState, PsiElement lastParent, @Nonnull PsiElement source)
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
