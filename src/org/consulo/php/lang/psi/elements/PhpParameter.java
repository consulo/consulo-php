package org.consulo.php.lang.psi.elements;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 3, 2008 10:32:20 PM
 */
public interface PhpParameter extends PHPPsiElement, PhpNamedElement, PhpTypedElement
{
	PhpParameter[] EMPTY_ARRAY = new PhpParameter[0];
}
