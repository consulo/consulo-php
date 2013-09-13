package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 3:36:12 PM
 */
public interface Foreach extends PHPPsiElement
{

	public PsiElement getArray();

	public PhpVariableReference getKey();

	public PhpVariableReference getValue();

}
