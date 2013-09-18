package org.consulo.php.lang.psi;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 11, 2008 9:55:46 PM
 */
public interface PhpClassReference extends PhpElement, PsiPolyVariantReference
{
	String getReferenceName();
}
