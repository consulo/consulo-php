package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface GroupStatement extends PhpElement
{

	public PsiElement[] getStatements();

}
