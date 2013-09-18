package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface PhpFunction extends PhpElement, PhpNamedElement, PhpTypedElement
{

	public PhpParameter[] getParameters();

	public PhpParameterList getParameterList();

}
