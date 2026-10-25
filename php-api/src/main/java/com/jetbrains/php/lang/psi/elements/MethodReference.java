package com.jetbrains.php.lang.psi.elements;

import jakarta.annotation.Nonnull;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;

import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date May 15, 2008 12:35:16 PM
 */
public interface MethodReference extends PhpPsiElement, PsiPolyVariantReference, PhpTypedElement, ParameterListOwner
{
	/**
	 * @return what this reference stands for, e.g. {@code #M#C\\Foo.bar}; several alternatives joined with {@code |}
	 */
	@Nonnull
	String getSignature();

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
