package org.consulo.php.lang.psi;

/**
 * @author jay
 * @date Apr 8, 2008 1:53:42 PM
 */
public interface PhpClass extends PhpModifierListOwner, PhpMemberHolder
{
	PhpClass[] EMPTY_ARRAY = new PhpClass[0];

	static String CONSTRUCTOR = "__construct";

	PhpClass getSuperClass();

	PhpClass[] getImplementedInterfaces();

	PhpFunction getConstructor();

	boolean isInterface();

	boolean isTrait();
}
