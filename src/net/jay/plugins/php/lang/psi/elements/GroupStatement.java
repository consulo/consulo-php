package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface GroupStatement extends PHPPsiElement {

  public PsiElement[] getStatements();

}
