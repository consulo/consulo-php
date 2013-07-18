package net.jay.plugins.php.lang.psi.elements;

import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Apr 3, 2008 11:08:55 PM
 */
public interface PhpMethod extends PHPPsiElement, PhpNamedElement,  PhpTypedElement
{
	@NotNull
	public PhpParameter[] getParameters();

	public PhpParameterList getParameterList();

	public PhpModifier getModifier();

	public boolean isStatic();
}
