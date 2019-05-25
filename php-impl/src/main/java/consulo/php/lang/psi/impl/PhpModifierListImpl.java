package consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.PhpModifierList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

import javax.annotation.Nonnull;

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

	@Override
	public PhpModifier.Access getAccess()
	{
		if(hasModifier(PhpTokenTypes.PRIVATE_KEYWORD))
		{
			return PhpModifier.Access.PRIVATE;
		}
		if(hasModifier(PhpTokenTypes.PROTECTED_KEYWORD))
		{
			return PhpModifier.Access.PROTECTED;
		}
		return PhpModifier.Access.PUBLIC;
	}
}
