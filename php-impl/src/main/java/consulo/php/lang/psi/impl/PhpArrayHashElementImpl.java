package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.util.ObjectUtil;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-12
 */
public class PhpArrayHashElementImpl extends PhpElementImpl implements ArrayHashElement
{
	public PhpArrayHashElementImpl(ASTNode node)
	{
		super(node);
	}

	@Nonnull
	@Override
	public PhpPsiElement getKey()
	{
		return getFirstPsiChild();
	}

	@Nonnull
	@Override
	public PhpPsiElement getValue()
	{
		PhpPsiElement firstPsiChild = getFirstPsiChild();
		return ObjectUtil.notNull(firstPsiChild.getNextPsiSibling());
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitArrayHashElement(this);
	}
}
