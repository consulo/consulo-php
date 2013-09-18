package org.consulo.php.lang.psi;

/**
 * @author jay
 * @date May 5, 2008 8:06:33 AM
 */
public interface PhpGlobal extends PhpElement
{

	public PhpVariableReference[] getVariables();

}
