package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpModifierList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public class PhpModifierListImpl extends PhpElementImpl implements PhpModifierList
{
	public PhpModifierListImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitModifierList(this);
	}

	@Override
	public boolean hasModifier(@Nonnull IElementType type)
	{
		return findChildByType(type) != null;
	}

	@Override
	public boolean hasModifier(@Nonnull TokenSet tokenSet)
	{
		return findChildByType(tokenSet) != null;
	}
}
