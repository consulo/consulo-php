package net.jay.plugins.php.lang.psi.elements;

import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date May 5, 2008 9:11:46 AM
 */
public interface PhpField extends PHPPsiElement, PhpNamedElement, PhpTypedElement
{
	PhpModifier getModifier();
}
