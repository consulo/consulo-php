package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpWhileStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;

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
