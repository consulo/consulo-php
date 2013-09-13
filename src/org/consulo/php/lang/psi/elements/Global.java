package org.consulo.php.lang.psi.elements;

/**
 * @author jay
 * @date May 5, 2008 8:06:33 AM
 */
public interface Global extends PHPPsiElement
{

	public PhpVariableReference[] getVariables();

}
