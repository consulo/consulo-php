package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.php.lang.psi.elements.BinaryExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 4, 2008 10:55:12 AM
 */
public class PhpBinaryExpressionImpl extends PhpElementImpl implements BinaryExpression
{
	private static final Logger LOGGER = Logger.getInstance(PhpBinaryExpressionImpl.class);

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
		PhpPsiElement firstPsiChild = getFirstPsiChild();
		if(firstPsiChild == null)
		{
			LOGGER.error("Expression: " + getText() + " is not have right operand");
			return null;
		}
		return firstPsiChild.getNextPsiSibling();
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
