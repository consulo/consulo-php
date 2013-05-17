package net.jay.plugins.php.cache.psi;

import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date Jun 16, 2008 9:47:16 PM
 */
public class LightPhpMethod extends LightPhpElement implements LightPhpElementWithModifier {

  private PhpModifier modifier;
  private String typeString = "";

  public LightPhpMethod(LightPhpElement parent, String name) {
    super(parent, name);
  }

  public PhpModifier getModifier() {
    return modifier;
  }

  public void setModifier(PhpModifier modifier) {
    this.modifier = modifier;
  }

  public String getTypeString() {
    return typeString;
  }

  public void setTypeString(String typeString) {
    this.typeString = typeString;
  }

  public void accept(@NotNull LightPhpElementVisitor visitor) {
    visitor.visitMethod(this);
  }

  public boolean equals(Object obj) {
    if (obj instanceof LightPhpMethod) {
      return ((LightPhpMethod) obj).getParentOfType(LightPhpClass.class) == getParentOfType(LightPhpClass.class)
        && ((LightPhpMethod) obj).getName().equals(getName());
    }
    return super.equals(obj);
  }

  public String toString() {
    return getClass().getSimpleName() + ": " + getName();
  }
}
