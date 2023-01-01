package com.jetbrains.php.lang.psi.elements;

import consulo.language.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 3:36:12 PM
 */
public interface PhpForeachStatement extends PhpPsiElement
{
	public PsiElement getArray();

	public Variable getKey();

	public Variable getValue();
}
