package com.jetbrains.php.lang.psi.elements;

import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date May 11, 2008 9:55:46 PM
 */
public interface ClassReference extends PhpPsiElement, PsiPolyVariantReference
{
	@Nullable
	ClassReference getQualifier();

	@Nullable
	String getReferenceName();

	@Nullable
	PsiElement getReferenceElement();

	String getName();

	@Nonnull
	PhpType resolveLocalType();

	boolean isAbsolute();
}
