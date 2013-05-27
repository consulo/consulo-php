package net.jay.plugins.php.lang.psi.elements;

import javax.swing.Icon;

/**
 * @author jay
 * @date Apr 8, 2008 1:53:42 PM
 */
public interface PhpClass extends PHPPsiElement, PhpNamedElement, LightCopyContainer
{

	public static String CONSTRUCTOR = "__construct";

	public Field[] getFields();

	public Method[] getMethods();

	public PhpClass getSuperClass();

	public PhpInterface[] getImplementedInterfaces();

	public Method getConstructor();

	public Icon getIcon();

	public boolean isAbstract();

	public boolean isFinal();

}
