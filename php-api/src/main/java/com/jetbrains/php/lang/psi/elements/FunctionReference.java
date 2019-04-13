package com.jetbrains.php.lang.psi.elements;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 15, 2008 12:34:38 PM
 */
public interface FunctionReference extends PhpPsiElement, PsiPolyVariantReference
{
	boolean canReadName();

	String getFunctionName();

	ParameterList getParameterList();
}
