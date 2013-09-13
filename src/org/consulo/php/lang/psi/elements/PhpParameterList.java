package org.consulo.php.lang.psi.elements;

import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 15, 2008 1:59:56 PM
 */
public interface PhpParameterList extends PhpElement
{
	@NotNull
	PhpParameter[] getParameters();
}
