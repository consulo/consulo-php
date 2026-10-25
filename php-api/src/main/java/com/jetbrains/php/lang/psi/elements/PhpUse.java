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

	/**
	 * @return {@code B} of {@code use A as B}
	 */
	@Nullable
	String getAliasName();

	/**
	 * @return imported FQN with the leading backslash, group prefix included
	 */
	@Nullable
	String getFQN();

	/**
	 * @return {@code true} for classes and namespaces, {@code false} for {@code use function} and {@code use const}
	 */
	boolean isClassImport();
}
