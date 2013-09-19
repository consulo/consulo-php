package org.consulo.php.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public interface PhpModifierListOwner extends PhpNamedElement {
	@Nullable
	PhpModifierList getModifierList();

	boolean hasModifier(@NotNull IElementType type);

	boolean hasModifier(@NotNull TokenSet tokenSet);
}
