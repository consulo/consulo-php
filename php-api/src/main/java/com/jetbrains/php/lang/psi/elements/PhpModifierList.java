package com.jetbrains.php.lang.psi.elements;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 18.09.13.
 */
public interface PhpModifierList extends PhpPsiElement
{
	boolean hasModifier(@Nonnull IElementType type);

	boolean hasModifier(@Nonnull TokenSet tokenSet);

	PhpModifier.Access getAccess();
}
