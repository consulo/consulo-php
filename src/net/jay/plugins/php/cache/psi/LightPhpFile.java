package net.jay.plugins.php.cache.psi;

import org.jetbrains.annotations.NotNull;
import net.jay.plugins.php.cache.PhpFileInfo;

/**
 * @author jay
 * @date May 29, 2008 11:48:03 PM
 */
public class LightPhpFile extends LightPhpElement {

  public LightPhpFile() {
  }

  public LightPhpFile(PhpFileInfo info) {
    super(info);
  }

  public void accept(@NotNull LightPhpElementVisitor visitor) {
    visitor.visitFile(this);
  }
}
