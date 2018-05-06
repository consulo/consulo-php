package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpNewExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 18, 2008 1:34:34 AM
 */
public class PhpNewExpressionImpl extends PhpTypeOwnerImpl implements PhpNewExpression
{
	public PhpNewExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitNewExpression(this);
	}
}
