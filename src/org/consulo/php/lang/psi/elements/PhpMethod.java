package org.consulo.php.lang.psi.elements;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 3, 2008 11:08:55 PM
 */
public interface PhpMethod extends PhpElement, PhpNamedElement,  PhpTypedElement
{
	@NotNull
	public PhpParameter[] getParameters();

	public PhpParameterList getParameterList();

	public PhpModifier getModifier();

	public boolean isStatic();
}
