package com.jetbrains.php.lang.psi.elements;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpUse extends PhpPsiElement
{
	@Nonnull
	ClassReference[] getClassReferences();

	@Nullable
	String getAliasName();
}
