package com.jetbrains.php.lang.psi.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;

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
}
