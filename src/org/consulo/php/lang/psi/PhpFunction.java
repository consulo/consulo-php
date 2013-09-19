package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.resolve.types.PhpTypeOwner;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface PhpFunction extends PhpModifierListOwner, PhpTypeOwner
{
	PhpFunction[] EMPTY_ARRAY = new PhpFunction[0];

	PhpParameter[] getParameters();

	PhpParameterList getParameterList();
}
