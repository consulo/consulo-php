package com.jetbrains.php.lang.psi.elements;

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;

import javax.annotation.Nullable;

/**
 * @author jay
 * @date May 5, 2008 9:11:46 AM
 */
public interface Field extends PhpClassMember, RWAccess
{
	Field[] EMPTY = new Field[0];
	ArrayFactory<Field> ARRAY_FACTORY = count -> count > 0 ? new Field[count] : EMPTY;
	Condition<PsiElement> INSTANCEOF = use -> use instanceof Field;

	boolean isConstant();

	boolean isStatic();

	@Nullable
	PsiElement getDefaultValue();

	@Nullable
	String getDefaultValuePresentation();
}
