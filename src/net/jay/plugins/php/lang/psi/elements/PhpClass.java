package net.jay.plugins.php.lang.psi.elements;

/**
 * @author jay
 * @date Apr 8, 2008 1:53:42 PM
 */
public interface PhpClass extends PHPPsiElement, PhpNamedElement
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
