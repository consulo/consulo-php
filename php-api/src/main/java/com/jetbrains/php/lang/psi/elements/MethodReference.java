package com.jetbrains.php.lang.psi.elements;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;

import javax.annotation.Nullable;

/**
 * @author jay
 * @date May 15, 2008 12:35:16 PM
 */
public interface MethodReference extends PhpPsiElement, PsiPolyVariantReference, PhpTypedElement
{
	boolean canReadName();

	String getMethodName();

	@Nullable
	ClassReference getClassReference();

	@Nullable
	PsiElement getObjectReference();

	boolean isStatic();

	PsiElement getNameIdentifier();

	String getNamespaceName();

	String getName();
}
