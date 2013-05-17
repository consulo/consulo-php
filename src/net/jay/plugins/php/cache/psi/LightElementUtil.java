package net.jay.plugins.php.cache.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiElement;
import net.jay.plugins.php.cache.DeclarationsIndex;
import net.jay.plugins.php.lang.psi.PHPFile;
import net.jay.plugins.php.lang.psi.elements.LightCopyContainer;
import net.jay.plugins.php.lang.psi.elements.Method;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.util.PhpProjectUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jay
 * @date Jun 6, 2008 12:04:23 PM
 */
public class LightElementUtil {

  @SuppressWarnings({"ConstantConditions"})
  public static LightPhpClass findLightClassByName(@NotNull final String name, @NotNull final String url) {
    Project fileProject = PhpProjectUtil.getProjectByUrl(url);
    if (fileProject == null) {
      return null;
    }
    final PsiFile psiFile = PsiManager.getInstance(fileProject).findFile(VirtualFileManager.getInstance().findFileByUrl(url));
    final DeclarationsIndex index = DeclarationsIndex.getInstance(psiFile);
    if (index == null) {
      return null;
    }
    final List<LightPhpClass> classList = index.getClassesByName(name);
    if (classList.size() == 1) {
      return classList.get(0);
    }
    return null;
  }

  @SuppressWarnings({"ConstantConditions"})
  public static LightPhpInterface findLightInterfaceByName(@NotNull final String name, @NotNull final String url) {
    Project fileProject = PhpProjectUtil.getProjectByUrl(url);
    if (fileProject == null) {
      return null;
    }
    final PsiFile psiFile = PsiManager.getInstance(fileProject).findFile(VirtualFileManager.getInstance().findFileByUrl(url));
    final DeclarationsIndex index = DeclarationsIndex.getInstance(psiFile);
    if (index == null) {
      return null;
    }
    final List<LightPhpInterface> interfaceList = index.getInterfacesByName(name);
    if (interfaceList.size() == 1) {
      return interfaceList.get(0);
    }
    return null;
  }

  @SuppressWarnings({"ConstantConditions"})
  @Nullable
  public static LightPhpClass findLightClassByPsi(@NotNull final PhpClass phpClass) {
    final DeclarationsIndex index = DeclarationsIndex.getInstance(phpClass);
    if (index == null || phpClass.getName() == null) return null;
    final List<LightPhpClass> classList = index.getClassesByName(phpClass.getName());
    LightPhpClass lightClass = null;
    for (LightPhpClass klass : classList) {
      String phpClassUrl = null;
      if (phpClass.getContainingFile().isPhysical()) {
        phpClassUrl = phpClass.getContainingFile().getVirtualFile().getUrl();
      }
      if (phpClassUrl == null && phpClass.getContainingFile().getOriginalFile() != null && phpClass.getContainingFile().getOriginalFile().isPhysical()) {
        phpClassUrl = phpClass.getContainingFile().getOriginalFile().getVirtualFile().getUrl();
      }
      if (phpClassUrl == null) {
        System.out.println("Null PhpClass url");
        System.out.println("Class name: " + phpClass.getName());
      }
      if (klass.getVirtualFile().getUrl().equals(phpClassUrl)) {
        lightClass = klass;
        break;
      }
    }
    return lightClass;
  }

  public static LightPhpMethod findLightMethodByPsi(@NotNull final Method method) {
    final PsiElement psiElement = method.getParent();
    if (psiElement instanceof PhpClass) {
      LightPhpClass klass = findLightClassByPsi((PhpClass) psiElement);
      if (klass != null) {
        for (LightPhpMethod lightMethod : klass.getChildrenOfType(LightPhpMethod.class)) {
          if (lightMethod.getName().equals(method.getName())) {
            return lightMethod;
          }
        }
      }
    }
    return null;
  }

  /**
   * Returns PsiFile by VirtualFile
   *
   * @param file    VirtualFile
   * @param project Current project
   * @return PsiElement - PsiFile if found, null otherwise
   */
  @Nullable
  public static PsiFile getPsiFile(@NotNull final VirtualFile file, @NotNull final Project project) {
    return PsiManager.getInstance(project).findFile(file);
  }

  /**
   * Returns psiElement by RVirtualStrucuturalElement
   *
   * @param element virtual element to find
   * @param project Current project
   * @return PsiElement - Real psi element, corresponding virtual element, or null, if nothing found
   */
  @Nullable
  public static PHPPsiElement findPsiByLightElement(@NotNull final LightPhpElement element,
                                                      @NotNull final Project project) {
    VirtualFile virtualFile = element.getVirtualFile();
    if (virtualFile == null) {
      return null;
    }

    final PsiFile file = getPsiFile(virtualFile, project);
    if (!(file instanceof PHPFile)) {
      return null;
    }

    return findByPath((PHPFile) file, createStructurePath(element));
  }

  public static LinkedList<Integer> createStructurePath(@NotNull final LightPhpElement anchor) {
    final LinkedList<Integer> path = new LinkedList<Integer>();
    LightPhpElement current = anchor;
    do {
      final LightPhpElement parent = current.getParent();
      if (parent != null) {
        path.addFirst(parent.indexOf(current));
      }
      current = parent;
    } while (current != null);
    return path;
  }

  @Nullable
  private static PHPPsiElement findByPath(@NotNull final PHPFile root,
                                          @NotNull final List<Integer> path) {
    PHPPsiElement element = root;
    for (Integer index : path) {
      List<LightCopyContainer> elements = element.getChildrenForCache();
      if (0 <= index && index < elements.size()) {
        element = (PHPPsiElement) elements.get(index);
      } else {
        return null;
      }
    }
    return element;
  }

}
