package com.jetbrains.php.lang.psi.elements;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date Jun 18, 2008 1:51:17 PM
 */
public interface ClassConstantReference extends PhpPsiElement, PsiPolyVariantReference, PhpTypedElement
{
	@Nonnull
	String getName();

	@Nullable
	PsiElement getNameIdentifier();

	ClassReference getClassReference();
}
