package org.consulo.php.lang.psi;

import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 11, 2008 9:55:46 PM
 */
public interface PhpClassReference extends PhpElement, PsiPolyVariantReference
{
	@Nullable
	PhpClassReference getQualifier();

	@Nullable
	String getReferenceName();

	@Nullable
	PsiElement getReferenceElement();
}
