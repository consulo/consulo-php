package com.jetbrains.php.lang.psi.elements;

import com.jetbrains.php.codeInsight.PhpScopeHolder;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import consulo.annotation.access.RequiredReadAction;
import consulo.util.collection.ArrayFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date Apr 3, 2008 10:15:06 PM
 */
public interface Function extends PhpElementWithModifier, PhpTypedElement, PhpNamedElement, ParameterListOwner, PhpAttributesOwner, PhpScopeHolder
{
	Function[] EMPTY_ARRAY = new Function[0];

	ArrayFactory<Function> ARRAY_FACTORY = i -> i == 0 ? EMPTY_ARRAY : new Function[i];

	@Override
	@Nonnull
	@RequiredReadAction
	Parameter[] getParameters();

	@Nullable
	@Override
	default Parameter getParameter(int index)
	{
		Parameter[] parameters = getParameters();
		return index >= 0 && index < parameters.length ? parameters[index] : null;
	}

	/**
	 * @return true for {@code function () {}} and {@code fn() => x}
	 */
	default boolean isClosure()
	{
		return getNode().getElementType() == PhpElementTypes.CLOSURE;
	}
}
