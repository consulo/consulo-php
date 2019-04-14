package com.jetbrains.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 4:07:54 PM
 */
public interface Catch extends PhpPsiElement
{
	public Variable getException();

	public ClassReference getExceptionType();

	public PsiElement getStatement();
}
