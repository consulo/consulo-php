package consulo.php.lang.psi.impl;

import consulo.php.lang.psi.PhpClassReference;
import consulo.php.lang.psi.PhpUseStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import javax.annotation.Nonnull;
import com.intellij.lang.ASTNode;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpUseStatementImpl extends PhpElementImpl implements PhpUseStatement
{
	public PhpUseStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUseStatement(this);
	}

	@Nonnull
	@Override
	public PhpClassReference[] getClassReferences()
	{
		return findChildrenByClass(PhpClassReference.class);
	}
}
