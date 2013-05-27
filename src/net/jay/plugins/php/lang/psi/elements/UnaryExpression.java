package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:09:42 PM
 */
public interface UnaryExpression extends PHPPsiElement
{

	public PsiElement getExpression();

}
