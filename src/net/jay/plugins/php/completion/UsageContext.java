package net.jay.plugins.php.completion;

import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;

/**
 * @author jay
 * @date Jun 24, 2008 1:55:36 PM
 */
public class UsageContext {

  private PhpModifier modifier = null;
  private LightPhpClass classForAccessFilter = null;
  private LightPhpClass callingObjectClass = null;

  public UsageContext() {
    modifier = null;
    classForAccessFilter = null;
  }

  public UsageContext(PhpModifier modifier, LightPhpClass classForAccessFilter) {
    this.modifier = modifier;
    this.classForAccessFilter = classForAccessFilter;
  }

  public PhpModifier getModifier() {
    return modifier;
  }

  public void setModifier(PhpModifier modifier) {
    this.modifier = modifier;
  }

  public LightPhpClass getClassForAccessFilter() {
    return classForAccessFilter;
  }

  public void setClassForAccessFilter(LightPhpClass klass) {
    this.classForAccessFilter = klass;
  }

  public LightPhpClass getCallingObjectClass() {
    return callingObjectClass;
  }

  public void setCallingObjectClass(LightPhpClass callingObjectClass) {
    this.callingObjectClass = callingObjectClass;
  }
}
