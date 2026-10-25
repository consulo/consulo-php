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
	 * @return {@code CLASS} for classes and namespaces, {@code FUNCTION} for {@code use function}, {@code CONST} for {@code use const}
	 */
	@Nonnull
	PhpGroupUseElement.PhpUseKeyword getUseKeyword();

	/**
	 * @return {@code true} for classes and namespaces, {@code false} for {@code use function} and {@code use const}
	 */
	default boolean isClassImport()
	{
		return getUseKeyword() == PhpGroupUseElement.PhpUseKeyword.CLASS;
	}

	/**
	 * @return short name of the imported element: {@code B} of {@code use A\B as C}
	 */
	@Nullable
	default String getName()
	{
		String fqn = getFQN();
		return fqn == null ? null : fqn.substring(fqn.lastIndexOf('\\') + 1);
	}
}
