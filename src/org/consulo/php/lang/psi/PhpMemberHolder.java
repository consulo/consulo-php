package org.consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpMemberHolder extends PhpElement
{
	@NotNull
	PhpClass[] getClasses();

	@NotNull
	PhpFunction[] getFunctions();

	@NotNull
	PhpField[] getFields();
}
