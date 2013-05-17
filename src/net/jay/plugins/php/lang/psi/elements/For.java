package net.jay.plugins.php.lang.psi.elements;

/**
 * @author jay
 * @date Jul 2, 2008 3:21:58 AM
 */
public interface For extends PHPPsiElement {

  public PHPPsiElement getInitialExpression();
  public PHPPsiElement getConditionalExpression();
  public PHPPsiElement getRepeatedExpression();
  public PHPPsiElement getStatement();

}
