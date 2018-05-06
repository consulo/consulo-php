package consulo.php.lang.psi;

import consulo.php.lang.lexer.PhpTokenTypes;
import javax.annotation.Nonnull;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public interface PhpModifierList extends PhpElement
{
	TokenSet ACCESS_TOKENS = TokenSet.create(PhpTokenTypes.PUBLIC_KEYWORD, PhpTokenTypes.PROTECTED_KEYWORD, PhpTokenTypes.PRIVATE_KEYWORD);

	TokenSet MODIFIERS = TokenSet.create(PhpTokenTypes.PUBLIC_KEYWORD, PhpTokenTypes.PROTECTED_KEYWORD, PhpTokenTypes.PRIVATE_KEYWORD,
			PhpTokenTypes.STATIC_KEYWORD, PhpTokenTypes.FINAL_KEYWORD, PhpTokenTypes.ABSTRACT_KEYWORD);

	boolean hasModifier(@Nonnull IElementType type);

	boolean hasModifier(@Nonnull TokenSet tokenSet);
}
