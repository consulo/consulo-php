package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import consulo.php.lang.psi.PhpUse;
import consulo.php.lang.psi.PhpUseListStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public class PhpUseListStatementImpl extends PhpElementImpl implements PhpUseListStatement
{
	public PhpUseListStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpUse[] getUses()
	{
		return findChildrenByClass(PhpUse.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUseList(this);
	}
}
