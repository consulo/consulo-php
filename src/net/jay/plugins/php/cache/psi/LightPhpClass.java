package net.jay.plugins.php.cache.psi;

import com.intellij.openapi.vfs.VirtualFile;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 6, 2008 11:47:05 AM
 */
public class LightPhpClass extends LightPhpElement implements LightPhpElementWithModifier {

  private PhpModifier modifier;
  private LightPhpClass superClass = null;
  private List<LightPhpInterface> superInterfaces = null;
  private String superClassName;
  private List<String> superInterfaceNames = new ArrayList<String>();
  private boolean hasStaticMembers = false;

  public LightPhpClass(LightPhpElement parent, String name) {
    super(parent, name);
  }

  public LightPhpClass getSuperClass() {
    if (superClass == null && superClassName != null) {
      superClass = LightElementUtil.findLightClassByName(superClassName, getVirtualFile().getUrl());
    }
    return superClass;
  }

  public List<LightPhpClass> getAllSuperClasses() {
    List<LightPhpClass> result = new ArrayList<LightPhpClass>();
    LightPhpClass superClass = getSuperClass();
    while (superClass != null) {
      result.add(superClass);
      superClass = superClass.getSuperClass();
    }
    return result;
  }

  public void setSuperClassName(String name) {
    if (!name.equals(getName())) {
      superClassName = name;
    }
  }

  public List<LightPhpInterface> getSuperInterfaces() {
    if (superInterfaces == null) {
      superInterfaces = new ArrayList<LightPhpInterface>();
      for (String interfaceName : superInterfaceNames) {
        LightPhpInterface item = LightElementUtil.findLightInterfaceByName(interfaceName, getVirtualFile().getUrl());
        if (item != null) {
          superInterfaces.add(item);
        } else {
        }
      }
    }
    return superInterfaces;
  }

  public void addSuperInterface(String name) {
    if (!name.equals(getName())) {
      superInterfaceNames.add(name);
    }
  }

  private List<String> getMethodNames() {
    List<String> result = new ArrayList<String>();
    for (LightPhpMethod method : getChildrenOfType(LightPhpMethod.class)) {
      result.add(method.getName());
    }
    return result;
  }

  private List<String> getFieldNames() {
    List<String> result = new ArrayList<String>();
    for (LightPhpField field : getChildrenOfType(LightPhpField.class)) {
      result.add(field.getName());
    }
    return result;
  }

  public List<LightPhpField> getFields() {
    List<LightPhpField> fields = getChildrenOfType(LightPhpField.class);
    List<String> fieldNames = getFieldNames();
    LightPhpClass parent = getSuperClass();
    if (parent != null) {
      for (LightPhpField parentField : parent.getFields()) {
        if (!fieldNames.contains(parentField.getName())) {
          fields.add(parentField);
        }
      }
    }
    return fields;
  }

  public List<LightPhpMethod> getMethods() {
    List<LightPhpMethod> methods = getChildrenOfType(LightPhpMethod.class);
    List<String> methodNames = getMethodNames();
    LightPhpClass parent = getSuperClass();
    if (parent != null) {
      for (LightPhpMethod parentMethod : parent.getMethods()) {
        if (!methodNames.contains(parentMethod.getName())) {
          methods.add(parentMethod);
        }
      }
    }
    final List<LightPhpInterface> interfaceList = getSuperInterfaces();
    for (LightPhpInterface phpInterface : interfaceList) {
      for (LightPhpMethod interfaceMethod : phpInterface.getMethods()) {
        if (!methodNames.contains(interfaceMethod.getName())) {
          methods.add(interfaceMethod);
        }
      }
    }
    return methods;
  }

  public LightPhpMethod getConstructor() {
    LightPhpMethod newOne = null;
    LightPhpMethod oldOne = null;
    for (LightPhpMethod method : getChildrenOfType(LightPhpMethod.class)) {
      if (method.getName().equals(PhpClass.CONSTRUCTOR)) {
        newOne = method;
      }
      if (method.getName().equals(getName())) {
        oldOne = method;
      }
    }
    if (newOne != null) {
      return newOne;
    }
    return oldOne;
  }

  public void accept(@NotNull LightPhpElementVisitor visitor) {
    visitor.visitClass(this);
  }

  public boolean equals(Object obj) {
    if (obj instanceof LightPhpClass) {
      final VirtualFile myVirtualFile = getVirtualFile();
      final VirtualFile otherVirtualFile = ((LightPhpClass) obj).getVirtualFile();
      if (myVirtualFile == null || otherVirtualFile == null) {
        return false;
      }
      return otherVirtualFile.getUrl().equals(myVirtualFile.getUrl())
        && ((LightPhpClass) obj).getName().equals(getName());
    }
    return super.equals(obj);
  }

  public PhpModifier getModifier() {
    return modifier;
  }

  public void setModifier(PhpModifier modifier) {
    this.modifier = modifier;
  }

  public void registerChild(LightPhpElement child) {
    if (child instanceof LightPhpMethod) {
      if (((LightPhpMethod) child).getModifier().isStatic()) {
        hasStaticMembers = true;
      }
    }
  }

  public boolean hasStaticMembers() {
    return hasStaticMembers;
  }
}
