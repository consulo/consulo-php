package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 4:07:54 PM
 */
public interface Catch extends PHPPsiElement
{

	public PhpVariableReference getException();

	public PhpClassReference getExceptionType();

	public PsiElement getStatement();

}
