package net.jay.plugins.php.lang.psi.elements;

/**
 * @author jay
 * @date May 5, 2008 8:06:33 AM
 */
public interface Global extends PHPPsiElement {

  public Variable[] getVariables();

}
