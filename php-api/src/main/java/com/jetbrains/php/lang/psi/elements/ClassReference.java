package com.jetbrains.php.lang.psi.elements;

import javax.annotation.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;

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
}
