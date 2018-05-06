package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpTernaryExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;

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
