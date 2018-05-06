package consulo.php.lang.psi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public interface PhpModifierListOwner extends PhpNamedElement
{
	@Nullable
	PhpModifierList getModifierList();

	boolean hasModifier(@Nonnull IElementType type);

	boolean hasModifier(@Nonnull TokenSet tokenSet);
}
