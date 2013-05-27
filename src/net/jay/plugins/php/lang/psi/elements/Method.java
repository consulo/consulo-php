package net.jay.plugins.php.lang.psi.elements;

import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 3, 2008 11:08:55 PM
 */
public interface Method extends PHPPsiElement, PhpNamedElement, LightCopyContainer, PhpTypedElement
{

	public Parameter[] getParameters();

	public ParameterList getParameterList();

	public PhpModifier getModifier();

	public boolean isStatic();

}
