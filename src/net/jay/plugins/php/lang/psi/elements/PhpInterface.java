package net.jay.plugins.php.lang.psi.elements;

import javax.swing.*;

/**
 * @author jay
 * @date Apr 8, 2008 1:58:55 PM
 */
public interface PhpInterface extends PHPPsiElement, PhpNamedElement, LightCopyContainer {

  public Icon getIcon();

  public Method[] getMethods();

}
