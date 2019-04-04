package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpUseImpl extends PhpElementImpl implements PhpUse
{
	public PhpUseImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUse(this);
	}

	@Nonnull
	@Override
	public ClassReference[] getClassReferences()
	{
		return findChildrenByClass(ClassReference.class);
	}
}
