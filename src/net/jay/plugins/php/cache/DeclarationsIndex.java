package net.jay.plugins.php.cache;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import net.jay.plugins.php.PHPBundle;
import net.jay.plugins.php.cache.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author jay
 * @date May 29, 2008 11:13:37 AM
 */
public class DeclarationsIndex {
  final private Project project;

  final private Map<String, IndexEntry> classIndex = new HashMap<String, IndexEntry>();
  final private Map<String, IndexEntry> interfaceIndex = new HashMap<String, IndexEntry>();
  final private Map<String, IndexEntry> functionIndex = new HashMap<String, IndexEntry>();
  final private Map<LightPhpClass, List<LightPhpClass>> subclasses = new com.intellij.util.containers.HashMap<LightPhpClass, List<LightPhpClass>>();
  private PhpModuleFilesCache filesCache = null;

  public DeclarationsIndex(@NotNull final Project project) {
    this.project = project;
  }

  public static DeclarationsIndex getInstance(PsiElement element) {
    Module module = ModuleUtil.findModuleForPsiElement(element);
    if (module == null) return null;

    PhpModuleCacheManager cacheManager = module.getComponent(PhpModuleCacheManager.class);
    if (cacheManager == null) return null;

    return cacheManager.getDeclarationsIndex();
  }

  public void setFileCache(@NotNull final PhpModuleFilesCache filesCache) {
    this.filesCache = filesCache;
  }

  public void build(final boolean runProcessWithProgressSynchronously) {
    final ProgressManager manager = ProgressManager.getInstance();

    final Runnable buildIndexRunnable = new Runnable() {
      public void run() {
        final ProgressIndicator indicator = manager.getProgressIndicator();
        if (runProcessWithProgressSynchronously) {
          if (indicator != null) {
            indicator.setIndeterminate(true);
          }
        }
        classIndex.clear();
        interfaceIndex.clear();
        buildIndexByCache(indicator);
      }
    };
    if (runProcessWithProgressSynchronously) {
      manager.runProcessWithProgressSynchronously(buildIndexRunnable, PHPBundle.message("progress.indicator.title.building.index"), false, project);
    } else {
      buildIndexRunnable.run();
    }
  }

  private void buildIndexByCache(ProgressIndicator indicator) {
    indicator.setIndeterminate(false);
    indicator.setText(PHPBundle.message("progress.indicator.title.building.index.message"));
    int size = filesCache.getAllUrls().size();
    int done = 0;
    for (String url : filesCache.getAllUrls()) {
      done++;
      indicator.setFraction((double) done / size);
      String displayPath = url;
      if (url.length() > 50) {
        displayPath = "..." + url.substring(url.length() - 50);
      }
      indicator.setText2(displayPath);
      final VirtualFileManager virtualFileManager = VirtualFileManager.getInstance();
      final VirtualFile fileByUrl = virtualFileManager.findFileByUrl(url);
      if (fileByUrl != null) {
        final PhpFileInfo fileInfo = filesCache.getUp2DateFileInfo(fileByUrl);
        if (fileInfo != null) {
          addFileInfoToIndex(fileInfo);
        }
      }
    }
  }

  /**
   * Adds info form fileInfo to shortNameIndex
   *
   * @param fileInfo PhpFileInfo to add information from
   */
  public void addFileInfoToIndex(@NotNull final PhpFileInfo fileInfo) {
    final LightPhpFile root = fileInfo.getLightPhpFile();

    final List<LightPhpClass> classes = new ArrayList<LightPhpClass>();
    final List<LightPhpInterface> interfaces = new ArrayList<LightPhpInterface>();
    final List<LightPhpFunction> functions = new ArrayList<LightPhpFunction>();
    root.accept(new RecursiveLightPhpElementVisitor() {
      public void visitClass(LightPhpClass klass) {
        classes.add(klass);
        super.visitClass(klass);
      }
      public void visitInterface(LightPhpInterface anInterface) {
        interfaces.add(anInterface);
        super.visitInterface(anInterface);
      }

      @Override
      public void visitFunction(LightPhpFunction function) {
        functions.add(function);
        super.visitFunction(function);
      }
    });

    for (LightPhpClass klass : classes) {
      addClassToIndex(klass);
    }
    for (LightPhpInterface anInterface : interfaces) {
      addInterfaceToIndex(anInterface);
    }
    for (LightPhpFunction function : functions) {
      addFunctionToIndex(function);
    }
  }

  public void removeFileInfoFromIndex(@Nullable final PhpFileInfo fileInfo) {
    if (fileInfo == null) {
      return;
    }

    final LightPhpFile root = fileInfo.getLightPhpFile();

    final List<LightPhpClass> classes = new ArrayList<LightPhpClass>();
    final List<LightPhpInterface> interfaces = new ArrayList<LightPhpInterface>();
    final List<LightPhpFunction> functions = new ArrayList<LightPhpFunction>();
    root.accept(new RecursiveLightPhpElementVisitor() {
      public void visitClass(LightPhpClass klass) {
        classes.add(klass);
      }
      public void visitInterface(LightPhpInterface anInterface) {
        interfaces.add(anInterface);
        super.visitInterface(anInterface);
      }

      @Override
      public void visitFunction(LightPhpFunction function) {
        functions.add(function);
        super.visitFunction(function);
      }
    });

    for (LightPhpClass klass : classes) {
      removeClassFromIndex(klass);
    }
    for (LightPhpInterface anInterface : interfaces) {
      removeInterfaceFromIndex(anInterface);
    }
    for (LightPhpFunction function : functions) {
      removeFunctionFromIndex(function);
    }
  }

  private void addFunctionToIndex(@NotNull final LightPhpFunction function) {
    final String name = function.getName();
    IndexEntry entry = functionIndex.get(name);
    if (entry == null) {
      entry = new IndexEntry();
      functionIndex.put(name, entry);
    }
    entry.addFunction(function);
  }

  private void removeFunctionFromIndex(@NotNull final LightPhpFunction function) {
    final String name = function.getName();
    IndexEntry entry = functionIndex.get(name);
    if (entry == null) {
      return;
    }
    entry.removeFunction(function);
    if (entry.getFunctions().isEmpty()) {
      functionIndex.remove(name);
    }
  }

  private void addClassToIndex(@NotNull final LightPhpClass klass) {
    final String name = klass.getName();
    IndexEntry entry = classIndex.get(name);
    if (entry == null) {
      entry = new IndexEntry();
      classIndex.put(name, entry);
    }
    entry.addClass(klass);
  }

  private void removeClassFromIndex(@NotNull final LightPhpClass klass) {
    final String name = klass.getName();
    IndexEntry entry = classIndex.get(name);
    if (entry == null) {
      return;
    }
    entry.removeClass(klass);
    if (entry.getClasses().isEmpty()) {
      classIndex.remove(name);
    }
  }

  @NotNull
  public List<LightPhpClass> getClassesByName(@NotNull final String name) {
    final IndexEntry entry = classIndex.get(name);
    if (entry != null) {
      return entry.getClasses();
    }
    return Collections.emptyList();
  }

  @NotNull
  public Collection<String> getAllClassNames() {
    return classIndex.keySet();
  }

  private void addInterfaceToIndex(@NotNull final LightPhpInterface anInterface) {
    final String name = anInterface.getName();
    IndexEntry entry = interfaceIndex.get(name);
    if (entry == null) {
      entry = new IndexEntry();
      interfaceIndex.put(name, entry);
    }
    entry.addInterface(anInterface);
  }

  private void removeInterfaceFromIndex(@NotNull final LightPhpInterface anInterface) {
    final String name = anInterface.getName();
    IndexEntry entry = interfaceIndex.get(name);
    if (entry == null) {
      return;
    }
    entry.removeInterface(anInterface);
    if (entry.getInterfaces().isEmpty()) {
      interfaceIndex.remove(name);
    }
  }

  public List<LightPhpInterface> getInterfacesByName(@NotNull final String name) {
    final IndexEntry entry = interfaceIndex.get(name);
    if (entry != null) {
      return entry.getInterfaces();
    }
    return Collections.emptyList();
  }

  @NotNull
  public Collection<String> getAllInterfaceNames() {
    return interfaceIndex.keySet();
  }

  public List<LightPhpFunction> getFunctionsByName(@NotNull final String name) {
    final IndexEntry entry = functionIndex.get(name);
    if (entry != null) {
      return entry.getFunctions();
    }
    return Collections.emptyList();
  }

  @NotNull
  public Collection<String> getAllFunctionNames() {
    return functionIndex.keySet();
  }

  @NotNull
  public List<LightPhpClass> getSubclasses(LightPhpClass klass) {
    List<LightPhpClass> result = subclasses.get(klass);
    if (result == null) {
      result = new ArrayList<LightPhpClass>();
      for (String className : getAllClassNames()) {
        for (LightPhpClass potentialSubclass : getClassesByName(className)) {
          if (potentialSubclass.getAllSuperClasses().contains(klass)) {
            result.add(potentialSubclass);
          }
        }
      }
      subclasses.put(klass, result);
    }
    return result;
  }

  /**
   * Used in debug purposes
   */
  @SuppressWarnings({"ConstantConditions"})
  public void printIndex() {
    final Collection<String> classNames = getAllClassNames();
    System.out.println("\n\nClass names: " + classNames.size());
    for (String s : classNames) {
      final List<LightPhpClass> classes = getClassesByName(s);
      System.out.println("  " + s + ": " + classes.size());
      for (LightPhpClass klass : classes) {
        System.out.print("    ");
        if (klass.getSuperClass() != null) {
          System.out.print("extends " + klass.getSuperClass().getName() + "; ");
        }
        System.out.print(klass.getVirtualFile().getUrl() + "\n");
      }
    }

  }
}
