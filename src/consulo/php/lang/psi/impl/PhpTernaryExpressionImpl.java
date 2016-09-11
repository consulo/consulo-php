package consulo.php.lang.psi.impl;

import consulo.php.lang.psi.PhpTernaryExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
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
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitTernaryExpression(this);
	}
}
