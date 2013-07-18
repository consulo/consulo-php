package net.jay.plugins.php.lang.psi.elements;

import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date Apr 3, 2008 9:55:25 PM
 */
public interface PhpVariableReference extends PHPPsiElement, PhpNamedElement, PsiPolyVariantReference, PhpTypedElement
{
	public boolean canReadName();

	public boolean isDeclaration();
}
