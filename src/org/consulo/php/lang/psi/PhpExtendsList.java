package org.consulo.php.lang.psi;

import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jun 7, 2008 7:03:18 PM
 */
public interface PhpExtendsList extends PhpElement
{

	@Nullable
	public PhpClass getExtendsClass();

}
