package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:09:42 PM
 */
public interface UnaryExpression extends PhpElement
{

	public PsiElement getExpression();

}
