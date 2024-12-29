package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-12
 */
public class PhpArrayAccessExpressionImpl extends PhpElementImpl implements ArrayAccessExpression
{
	public PhpArrayAccessExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}
}
