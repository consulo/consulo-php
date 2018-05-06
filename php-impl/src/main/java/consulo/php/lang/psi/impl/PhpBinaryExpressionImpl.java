package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.PhpBinaryExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @date Apr 4, 2008 10:55:12 AM
 */
public class PhpBinaryExpressionImpl extends PhpElementImpl implements PhpBinaryExpression
{
	public PhpBinaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getLeftOperand()
	{
		return getFirstPsiChild();
	}

	@Override
	public PsiElement getRightOperand()
	{
		return getFirstPsiChild().getNextPsiSibling();
	}

	@Override
	public IElementType getOperation()
	{
		return getNode().getChildren(PhpTokenTypes.tsOPERATORS)[0].getElementType();
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitBinaryExpression(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(lastParent == null || lastParent == getRightOperand())
		{
			if(lastParent != getRightOperand())
			{
				if(!getRightOperand().processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
			if(!getLeftOperand().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
