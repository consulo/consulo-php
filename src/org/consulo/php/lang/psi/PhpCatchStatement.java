package org.consulo.php.lang.psi;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 4:07:54 PM
 */
public interface PhpCatchStatement extends PhpElement
{

	public PhpVariableReference getException();

	public PhpClassReference getExceptionType();

	public PsiElement getStatement();

}
