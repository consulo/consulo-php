package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import consulo.php.lang.psi.PhpArrayExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-03-15
 */
public class PhpArrayExpressionImpl extends PhpElementImpl implements PhpArrayExpression
{
	public PhpArrayExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitArrayExpression(this);
	}
}
