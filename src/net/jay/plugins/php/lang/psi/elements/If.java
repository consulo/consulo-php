package net.jay.plugins.php.lang.psi.elements;

/**
 * @author jay
 * @date May 9, 2008 3:21:12 PM
 */
public interface If extends PHPPsiElement {

  public PHPPsiElement getCondition();
  public ElseIf[] getElseIfBranches();
  public Else getElseBranch();
  public PHPPsiElement getStatement();

}
