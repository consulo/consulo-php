package net.jay.plugins.php.util;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.RowIcon;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.EmptyIcon;
import net.jay.plugins.php.PHPIcons;
import net.jay.plugins.php.cache.psi.*;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.PhpInterface;
import net.jay.plugins.php.lang.psi.elements.PhpModifier;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

/**
 * @author jay
 * @date May 31, 2008 1:47:22 PM
 */
public class PhpPresentationUtil {

  @SuppressWarnings({"ConstantConditions"})
  private static String getPresentablePathForClass(@NotNull PhpClass klass) {
    VirtualFile classRoot = klass.getContainingFile().getVirtualFile();
    if (klass.getName() != null) {
      final String[] fileNames = ArrayUtil.reverseArray(klass.getName().split("_"));
      for (String fileName : fileNames) {
        if (!classRoot.getNameWithoutExtension().equals(fileName)) {
          break;
        }
        classRoot = classRoot.getParent();
      }
    }
    return getPresentablePathForFile(classRoot, klass.getProject());
  }

  @SuppressWarnings({"ConstantConditions"})
  private static String getPresentablePathForInterface(@NotNull PhpInterface phpInterface) {
    VirtualFile classRoot = phpInterface.getContainingFile().getVirtualFile();
    if (phpInterface.getName() != null) {
      final String[] fileNames = ArrayUtil.reverseArray(phpInterface.getName().split("_"));
      for (String fileName : fileNames) {
        if (!classRoot.getNameWithoutExtension().equals(fileName)) {
          break;
        }
        classRoot = classRoot.getParent();
      }
    }
    return getPresentablePathForFile(classRoot, phpInterface.getProject());
  }

  private static String getPresentablePathForFile(@NotNull VirtualFile file, Project project) {
    Module module = ModuleUtil.findModuleForFile(file, project);
    VirtualFile[] roots;
    if (module == null) {
      roots = new VirtualFile[]{project.getBaseDir()};
    } else {
      roots = ModuleRootManager.getInstance(module).getContentRoots();
    }
    String location = "";
    for (VirtualFile root : roots) {
      if (file.getUrl().startsWith(root.getUrl())) {
        location = VfsUtil.getRelativePath(file, root, File.separatorChar);
      }
    }
    location = ".../" + location;
    return location;
  }

  public static ItemPresentation getClassPresentation(PhpClass klass) {
    String location = getPresentablePathForClass(klass);
    return new PresentationData(klass.getName(), location, klass.getIcon(), klass.getIcon(), null);
  }

  public static ItemPresentation getInterfacePresentation(PhpInterface phpInterface) {
    String location = getPresentablePathForInterface(phpInterface);
    return new PresentationData(phpInterface.getName(), location, phpInterface.getIcon(), phpInterface.getIcon(), null);
  }

  public static ItemPresentation getFilePresentation(PHPFile phpFile) {
    final VirtualFile virtualFile = phpFile.getVirtualFile();
    assert virtualFile != null;
    String location = getPresentablePathForFile(virtualFile, phpFile.getProject());
    return new PresentationData(phpFile.getName(), location, phpFile.getIcon(0), phpFile.getIcon(0), null);
  }

  public static Icon getIcon(LightPhpElement element) {
    RowIcon result = new RowIcon(2);
    result.setIcon(new EmptyIcon(PHPIcons.CLASS.getIconWidth(), PHPIcons.CLASS.getIconHeight()), 0);

    if (element instanceof LightPhpClass) {
      final LightPhpClass klass = (LightPhpClass) element;
      final PhpModifier modifier = klass.getModifier();
      if (modifier.isAbstract()) {
        result.setIcon(PHPIcons.ABSTRACT_CLASS, 0);
      } else if (modifier.isFinal()) {
        result.setIcon(PHPIcons.FINAL_CLASS, 0);
      } else {
        result.setIcon(PHPIcons.CLASS, 0);
      }

    }
    if (element instanceof LightPhpInterface) {
      result.setIcon(PHPIcons.INTERFACE, 0);
    }
    if (element instanceof LightPhpFunction) {
      result.setIcon(PHPIcons.FIELD, 0);
    }
    if (element instanceof LightPhpField) {
      final LightPhpField field = (LightPhpField) element;
      final PhpModifier modifier = field.getModifier();
      if (modifier.isStatic()) {
        result.setIcon(PHPIcons.STATIC_FIELD, 0);
      } else {
        result.setIcon(PHPIcons.FIELD, 0);
      }
      result.setIcon(getAccessIcon(modifier), 1);
    }

    if (element instanceof LightPhpMethod) {
      final LightPhpMethod method = (LightPhpMethod) element;
      final PhpModifier modifier = method.getModifier();
      if (modifier.isStatic()) {
        result.setIcon(PHPIcons.STATIC_METHOD, 0);
      } else {
        result.setIcon(PHPIcons.METHOD, 0);
      }
      result.setIcon(getAccessIcon(modifier), 1);
    }
    return result;
  }

  public static Icon getAccessIcon(PhpModifier modifier) {
    if (modifier.isPublic()) {
      return PHPIcons.PUBLIC;
    } else if (modifier.isProtected()) {
      return PHPIcons.PROTECTED;
    } else {
      return PHPIcons.PRIVATE;
    }
  }
}
