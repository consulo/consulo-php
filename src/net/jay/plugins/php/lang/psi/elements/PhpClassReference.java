package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 11, 2008 9:55:46 PM
 */
public interface PhpClassReference extends PHPPsiElement, PsiPolyVariantReference
{
	String getReferenceName();
}
