package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpTernaryExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpTernaryExpressionImpl extends PhpElementImpl implements PhpTernaryExpression
{
	public PhpTernaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitTernaryExpression(this);
	}
}
