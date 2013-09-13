package org.consulo.php.lang.psi.elements;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date May 5, 2008 9:11:46 AM
 */
public interface PhpField extends PhpElement, PhpNamedElement, PhpTypedElement
{
	PhpModifier getModifier();
}
