package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiElement;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:13 AM
 */
public interface AssignmentExpression extends PHPPsiElement, PhpTypedElement {

  public PsiElement getVariable();
  
  public PsiElement getValue();

}
