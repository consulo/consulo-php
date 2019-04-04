package com.jetbrains.php.lang.psi.elements;

import com.intellij.util.ArrayFactory;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface Function extends PhpElementWithModifier, PhpTypedElement, PhpNamedElement
{
	Function[] EMPTY_ARRAY = new Function[0];

	ArrayFactory<Function> ARRAY_FACTORY = i -> i == 0 ? EMPTY_ARRAY : new Function[i];

	Parameter[] getParameters();

	ParameterList getParameterList();
}
