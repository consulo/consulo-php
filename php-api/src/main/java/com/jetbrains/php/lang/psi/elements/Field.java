package com.jetbrains.php.lang.psi.elements;

import com.intellij.util.ArrayFactory;
import consulo.annotations.RequiredReadAction;

/**
 * @author jay
 * @date May 5, 2008 9:11:46 AM
 */
public interface Field extends PhpElementWithModifier, PhpTypedElement, PhpClassMember
{

	Field[] EMPTY_ARRAY = new Field[0];
	ArrayFactory<Field> ARRAY_FACTORY = i -> i == 0 ? EMPTY_ARRAY : new Field[i];

	@RequiredReadAction
	boolean isConstant();
}
