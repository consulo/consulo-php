package org.consulo.php.lang.psi.resolve.types;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 17, 2008 2:24:52 PM
 */
public interface PhpTypedElement
{

	@NotNull
	public PhpType getType();

}
