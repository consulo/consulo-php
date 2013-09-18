package org.consulo.php.lang.psi;

/**
 * @author jay
 * @date Apr 8, 2008 1:53:42 PM
 */
public interface PhpClass extends PhpElement, PhpNamedElement
{
	static String CONSTRUCTOR = "__construct";

	PhpModifier getModifier();

	PhpField[] getFields();

	PhpMethod[] getMethods();

	PhpClass getSuperClass();

	PhpClass[] getImplementedInterfaces();

	PhpMethod getConstructor();

	boolean isAbstract();

	boolean isFinal();

	boolean isInterface();
}
