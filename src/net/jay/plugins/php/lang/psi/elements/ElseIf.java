package net.jay.plugins.php.lang.psi.elements;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:22 AM
 */
public interface ElseIf extends PHPPsiElement {

  public PHPPsiElement getCondition();

  public PHPPsiElement getStatement();

}
