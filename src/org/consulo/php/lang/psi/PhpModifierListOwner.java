package org.consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

	boolean hasModifier(@NotNull IElementType type);

	boolean hasModifier(@NotNull TokenSet tokenSet);
}
