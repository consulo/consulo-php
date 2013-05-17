package net.jay.plugins.php.cache.psi;

/**
 * @author jay
 * @date Jun 4, 2008 2:41:06 PM
 */
abstract public class LightPhpElementVisitor {

  public void visitElement(LightPhpElement element) {

  }

  public void visitFile(LightPhpFile file) {
    visitElement(file);
  }

  public void visitClass(LightPhpClass klass) {
    visitElement(klass);
  }

  public void visitInterface(LightPhpInterface anInterface) {
    visitElement(anInterface);
  }

  public void visitMethod(LightPhpMethod method) {
    visitElement(method);
  }

  public void visitField(LightPhpField field) {
    visitElement(field);
  }

  public void visitFunction(LightPhpFunction function) {
    visitElement(function);
  }
}
