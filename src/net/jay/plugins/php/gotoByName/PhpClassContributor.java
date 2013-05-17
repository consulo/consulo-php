package net.jay.plugins.php.gotoByName;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import net.jay.plugins.php.cache.DeclarationsIndex;
import net.jay.plugins.php.cache.PhpModuleCacheManager;
import net.jay.plugins.php.cache.psi.LightPhpClass;
import net.jay.plugins.php.cache.psi.LightPhpInterface;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date May 30, 2008 3:44:59 PM
 */
public class PhpClassContributor implements ChooseByNameContributor {

    /**
     * Returns the list of names for the specified project to which it is possible to navigate
     * by name.
     *
     * @param project                the project in which the navigation is performed.
     * @param includeNonProjectItems if true, the names of non-project items (for example,
     *                               library classes) should be included in the returned array.
     * @return the array of names.
     */
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        final ArrayList<String> names = new ArrayList<String>();
        final Module[] modules = ModuleManager.getInstance(project).getModules();

        for (Module module : modules) {
            PhpModuleCacheManager moduleCacheManager = module.getComponent(PhpModuleCacheManager.class);
            if (moduleCacheManager != null) {
                DeclarationsIndex index = moduleCacheManager.getDeclarationsIndex();
                names.addAll(index.getAllClassNames());
                names.addAll(index.getAllInterfaceNames());
            }
        }

        return names.toArray(new String[names.size()]);
    }

    /**
     * Returns the list of navigation items matching the specified name.
     *
     * @param name                   the name selected from the list.
     * @param pattern                the original pattern entered in the dialog
     * @param project                the project in which the navigation is performed.
     * @param includeNonProjectItems if true, the navigation items for non-project items (for example,
     *                               library classes) should be included in the returned array. @return the array of navigation items.
     */
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        final ArrayList<NavigationItem> items = new ArrayList<NavigationItem>();
        final Module[] modules = ModuleManager.getInstance(project).getModules();

        for (Module module : modules) {
            PhpModuleCacheManager moduleCacheManager = module.getComponent(PhpModuleCacheManager.class);
            if (moduleCacheManager != null) {
                DeclarationsIndex index = moduleCacheManager.getDeclarationsIndex();
                final List<LightPhpClass> classes = index.getClassesByName(name);
                for (LightPhpClass klass : classes) {
                    final PHPPsiElement element = klass.getPsi(project);
                    if (element != null) {
                        items.add((NavigationItem) element);
                    }
                }
                final List<LightPhpInterface> interfaces = index.getInterfacesByName(name);
                for (LightPhpInterface anInterface : interfaces) {
                    final PHPPsiElement element = anInterface.getPsi(project);
                    if (element != null) {
                        items.add((NavigationItem) element);
                    }
                }
            }
        }

        return items.toArray(new NavigationItem[items.size()]);
    }
}
