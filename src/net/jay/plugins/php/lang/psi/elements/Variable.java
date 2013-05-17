package net.jay.plugins.php.lang.psi.elements;

import com.intellij.psi.PsiPolyVariantReference;
import net.jay.plugins.php.lang.psi.resolve.types.PhpTypedElement;

/**
 * @author jay
 * @date Apr 3, 2008 9:55:25 PM
 */
public interface Variable extends PHPPsiElement,
                                  PhpNamedElement,
                                  PsiPolyVariantReference,
                                  PhpTypedElement {

  public boolean canReadName();

  public boolean isDeclaration();

}
