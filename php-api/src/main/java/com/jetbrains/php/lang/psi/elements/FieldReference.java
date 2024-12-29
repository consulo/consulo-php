package com.jetbrains.php.lang.psi.elements;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;

import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date May 15, 2008 11:23:18 AM
 */
public interface FieldReference extends PhpPsiElement, PsiReference
{
	boolean canReadName();

	@Nullable
	String getFieldName();

	@Nullable
	ClassReference getClassReference();

	@Nullable
	PsiElement getObjectReference();

	PsiElement getNameIdentifier();
}
