package com.jetbrains.php.lang.psi.elements;

/**
 * @author VISTALL
 * @since 07.07.13.
 */
public interface PhpNamespace extends PhpPsiElement
{
	String getNamespace();

	ClassReference getPackageReference();
}
