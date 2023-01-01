package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.util.lang.ObjectUtil;

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
