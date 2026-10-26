package com.jetbrains.php.lang.psi.elements;

import jakarta.annotation.Nonnull;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;

import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date May 15, 2008 11:23:18 AM
 */
public interface FieldReference extends MemberReference
{
	/**
	 * @return what this reference stands for, e.g. {@code #M#C\\Foo.bar}; several alternatives joined with {@code |}
	 */
	@Nonnull
	String getSignature();

	boolean canReadName();

	@Nullable
	String getFieldName();

	@Nullable
	@Override
	PhpExpression getClassReference();

	@Nullable
	PsiElement getObjectReference();

	PsiElement getNameIdentifier();
}
