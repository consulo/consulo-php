package com.jetbrains.php.lang.psi.elements;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;

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
}
