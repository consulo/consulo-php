package consulo.php.lang.psi.impl;

import consulo.php.lang.psi.PhpModifierList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
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
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitModifierList(this);
	}

	@Override
	public boolean hasModifier(@NotNull IElementType type)
	{
		return findChildByType(type) != null;
	}

	@Override
	public boolean hasModifier(@NotNull TokenSet tokenSet)
	{
		return findChildByType(tokenSet) != null;
	}
}
