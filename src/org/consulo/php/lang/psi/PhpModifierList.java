package org.consulo.php.lang.psi;

import org.consulo.php.lang.lexer.PhpTokenTypes;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public interface PhpModifierList extends PhpElement
{
	TokenSet ACCESS_TOKENS = TokenSet.create(PhpTokenTypes.PUBLIC_KEYWORD, PhpTokenTypes.PROTECTED_KEYWORD, PhpTokenTypes.PRIVATE_KEYWORD);

	TokenSet MODIFIERS = TokenSet.create(PhpTokenTypes.PUBLIC_KEYWORD, PhpTokenTypes.PROTECTED_KEYWORD, PhpTokenTypes.PRIVATE_KEYWORD, PhpTokenTypes.STATIC_KEYWORD, PhpTokenTypes.FINAL_KEYWORD);

	boolean hasModifier(@NotNull IElementType type);

	boolean hasModifier(@NotNull TokenSet tokenSet);
}
