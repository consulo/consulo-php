package org.consulo.php.lang.psi.elements;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;

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
