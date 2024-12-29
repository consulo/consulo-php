package com.jetbrains.php.lang.psi.elements;

import consulo.annotation.access.RequiredReadAction;
import consulo.util.collection.ArrayFactory;

import jakarta.annotation.Nonnull;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface Function extends PhpElementWithModifier, PhpTypedElement, PhpNamedElement, ParameterListOwner
{
	Function[] EMPTY_ARRAY = new Function[0];

	ArrayFactory<Function> ARRAY_FACTORY = i -> i == 0 ? EMPTY_ARRAY : new Function[i];

	@Override
	@Nonnull
	@RequiredReadAction
	Parameter[] getParameters();
}
