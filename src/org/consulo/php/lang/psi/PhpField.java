package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.resolve.types.PhpTypeOwner;

/**
 * @author jay
 * @date May 5, 2008 9:11:46 AM
 */
public interface PhpField extends PhpModifierListOwner, PhpTypeOwner
{
	PhpField[] EMPTY_ARRAY = new PhpField[0];

	boolean isConstant();
}
