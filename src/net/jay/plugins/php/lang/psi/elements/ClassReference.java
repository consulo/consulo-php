package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 11, 2008 9:55:46 PM
 */
public interface ClassReference extends PHPPsiElement, PsiPolyVariantReference
{

	public String getReferenceName();

}
