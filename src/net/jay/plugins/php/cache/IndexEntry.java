package net.jay.plugins.php.cache;

import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.cache.psi.LightPhpFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date May 29, 2008 11:20:13 AM
 */
public class IndexEntry {

  private List<LightPhpClass> classes = new ArrayList<LightPhpClass>();

  public void addClass(LightPhpClass klass) {
    classes.add(klass);
  }

  public void removeClass(LightPhpClass klass) {
    classes.remove(klass);
  }

  public List<LightPhpClass> getClasses() {
    return classes;
  }

  private List<LightPhpInterface> interfaces = new ArrayList<LightPhpInterface>();

  public void addInterface(LightPhpInterface anInterface) {
    interfaces.add(anInterface);
  }

  public void removeInterface(LightPhpInterface anInterface) {
    interfaces.remove(anInterface);
  }

  public List<LightPhpInterface> getInterfaces() {
    return interfaces;
  }

  private List<LightPhpFunction> functions = new ArrayList<LightPhpFunction>();

  public void addFunction(LightPhpFunction function) {
    functions.add(function);
  }

  public void removeFunction(LightPhpFunction function) {
    functions.remove(function);
  }

  public List<LightPhpFunction> getFunctions() {
    return functions;
  }

  /*private List<RVirtualClass> myClasses = new ArrayList<RVirtualClass>();
  private List<RVirtualModule> myModules = new ArrayList<RVirtualModule>();
  private List<RVirtualMethod> myMethods = new ArrayList<RVirtualMethod>();
  private List<RVirtualField> myFields = new ArrayList<RVirtualField>();
  private List<RVirtualConstant> myConstants = new ArrayList<RVirtualConstant>();
  private List<RVirtualGlobalVar> myGlobalVars = new ArrayList<RVirtualGlobalVar>();
  private List<RVirtualAlias> myAliases = new ArrayList<RVirtualAlias>();
  private List<RVirtualFieldAttr> myFieldAttrs = new ArrayList<RVirtualFieldAttr>();

  @NotNull
  public List<RVirtualClass> getClasses() {
    return myClasses;
  }

  @NotNull
  public List<RVirtualModule> getModules() {
    return myModules;
  }

  @NotNull
  public List<RVirtualMethod> getMethods() {
    return myMethods;
  }

  @NotNull
  public List<RVirtualField> getFields() {
    return myFields;
  }

  @NotNull
  public List<RVirtualConstant> getConstants() {
    return myConstants;
  }

  @NotNull
  public List<RVirtualGlobalVar> getGlobalVars() {
    return myGlobalVars;
  }

  @NotNull
  public List<RVirtualAlias> getAliases() {
    return myAliases;
  }

  @NotNull
  public List<RVirtualFieldAttr> getFieldAttrs() {
    return myFieldAttrs;
  }

  public boolean isEmpty() {
    return myClasses.isEmpty() &&
      myModules.isEmpty() &&
      myMethods.isEmpty() &&
      myConstants.isEmpty() &&
      myGlobalVars.isEmpty() &&
      myFields.isEmpty() &&
      myAliases.isEmpty() &&
      myFieldAttrs.isEmpty();
  }


  public void addContainer(@NotNull final RVirtualContainer container) {
    final StructureType type = container.getType();
    if (type.isMethod()) {
      addMethod((RVirtualMethod) container);
      return;
    }
    if (type == StructureType.CLASS) {
      addClass((RVirtualClass) container);
      return;
    }
    if (type == StructureType.MODULE) {
      addModule((RVirtualModule) container);
    }
  }

  private void addClass(@NotNull final RVirtualClass vClass) {
    myClasses.add(vClass);
  }

  private void addModule(@NotNull final RVirtualModule vModule) {
    myModules.add(vModule);
  }

  private void addMethod(@NotNull final RVirtualMethod vMethod) {
    myMethods.add(vMethod);
  }

  public void addConstant(@NotNull final RVirtualConstant constant) {
    myConstants.add(constant);
  }

  public void addGlobalVar(@NotNull final RVirtualGlobalVar globalVar) {
    myGlobalVars.add(globalVar);
  }

  public void addAlias(@NotNull final RVirtualAlias rVirtualAlias) {
    myAliases.add(rVirtualAlias);
  }

  public void addFieldAttr(@NotNull final RVirtualFieldAttr rVirtualFieldAttr) {
    myFieldAttrs.add(rVirtualFieldAttr);
  }

  public void addField(@NotNull final RVirtualField field) {
    myFields.add(field);
  }


  public void removeContainer(@NotNull final RVirtualContainer container) {
    final StructureType type = container.getType();
    if (type.isMethod()) {
      removeMethod((RVirtualMethod) container);
      return;
    }
    if (type == StructureType.CLASS) {
      removeClass((RVirtualClass) container);
      return;
    }
    if (type == StructureType.MODULE) {
      removeModule((RVirtualModule) container);
    }
  }

  private void removeModule(@NotNull final RVirtualModule rVirtualModule) {
    myModules.remove(rVirtualModule);
  }

  private void removeClass(@NotNull final RVirtualClass rVirtualClass) {
    myClasses.remove(rVirtualClass);
  }

  private void removeMethod(@NotNull final RVirtualMethod rVirtualMethod) {
    myMethods.remove(rVirtualMethod);
  }

  public void removeField(@NotNull final RVirtualField field) {
    myFields.remove(field);
  }

  public void removeConstant(@NotNull final RVirtualConstant constant) {
    myConstants.remove(constant);
  }

  public void removeGlobalVar(@NotNull final RVirtualGlobalVar globalVar) {
    myGlobalVars.remove(globalVar);
  }

  public void removeAlias(@NotNull final RVirtualAlias rVirtualAlias) {
    myAliases.remove(rVirtualAlias);
  }

  public void removeFieldAttr(@NotNull final RVirtualFieldAttr rVirtualFieldAttr) {
    myFieldAttrs.remove(rVirtualFieldAttr);
  }*/

}
