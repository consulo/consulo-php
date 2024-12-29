package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import com.jetbrains.php.lang.psi.elements.BinaryExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 4, 2008 10:55:12 AM
 */
public class PhpBinaryExpressionImpl extends PhpElementImpl implements BinaryExpression
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
		PhpPsiElement firstPsiChild = getFirstPsiChild();
		if(firstPsiChild == null)
		{
			//LOGGER.error("Expression: " + getText() + " is not have right operand");
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
		PsiElement rightOperand = getRightOperand();
		if(lastParent == null || lastParent == rightOperand)
		{
			if(lastParent != rightOperand)
			{
				if(!rightOperand.processDeclarations(processor, state, null, source))
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
