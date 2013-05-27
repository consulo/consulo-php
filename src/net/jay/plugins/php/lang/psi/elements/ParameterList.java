package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 1:59:56 PM
 */
public interface ParameterList extends PHPPsiElement
{

	public PsiElement[] getParameters();

}
