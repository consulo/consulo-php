package org.consulo.php.lang.psi.elements;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface Function extends PHPPsiElement, PhpNamedElement, PhpTypedElement
{

	public PhpParameter[] getParameters();

	public PhpParameterList getParameterList();

}
