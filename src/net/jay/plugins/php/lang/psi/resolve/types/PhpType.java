package net.jay.plugins.php.lang.psi.resolve.types;

import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author jay
 * @date Jun 17, 2008 10:56:45 PM
 */
public class PhpType implements Serializable {

  Collection<LightPhpClass> classes = new ArrayList<LightPhpClass>();

  public PhpType(LightPhpClass ... classes) {
    this.classes.addAll(Arrays.asList(classes));
  }

  public void addClasses(Collection<LightPhpClass> classes) {
    this.classes.addAll(classes);
  }

  public void addClass(LightPhpClass klass) {
    classes.add(klass);
  }

  @Nullable
  public LightPhpClass getType() {
    if (classes.size() == 1) {
      return classes.iterator().next();
    }
    return null;
  }

  @NotNull
  public Collection<LightPhpClass> getTypes() {
    return classes;
  }

  public Collection<LightPhpMethod> getMethods() {
    List<LightPhpMethod> methods = new ArrayList<LightPhpMethod>();
    for (LightPhpClass klass : classes) {
      methods.addAll(klass.getMethods());
    }
    return methods;
  }

  public String toString() {
    StringBuilder str = new StringBuilder();
    for (LightPhpClass klass : classes) {
      str.append(klass.getName()).append("|");
    }
    String s = str.toString();
    if (classes.size() > 0) {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }
}
