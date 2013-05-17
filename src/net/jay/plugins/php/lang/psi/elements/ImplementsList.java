package net.jay.plugins.php.lang.psi.elements;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface ImplementsList extends PHPPsiElement {

  @NotNull
  public List<PhpInterface> getInterfaces();
  
}
