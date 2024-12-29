package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpWhileStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 18, 2008 9:29:28 PM
 */
public class PhpWhileStatementImpl extends PhpElementImpl implements PhpWhileStatement
{
	public PhpWhileStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitWhileStatement(this);
	}
}
