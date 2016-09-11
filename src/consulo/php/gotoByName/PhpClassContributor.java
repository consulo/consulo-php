package consulo.php.gotoByName;

import java.util.Collection;

import consulo.php.index.PhpFullFqClassIndex;
import consulo.php.lang.psi.PhpClass;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author jay
 * @date May 30, 2008 3:44:59 PM
 */
public class PhpClassContributor implements ChooseByNameContributor
{

	/**
	 * Returns the list of names for the specified project to which it is possible to navigate
	 * by name.
	 *
	 * @param project                the project in which the navigation is performed.
	 * @param includeNonProjectItems if true, the names of non-project items (for example,
	 *                               library classes) should be included in the returned array.
	 * @return the array of names.
	 */
	@Override
	public String[] getNames(Project project, boolean includeNonProjectItems)
	{
		Collection<String> allKeys = PhpFullFqClassIndex.INSTANCE.getAllKeys(project);

		return allKeys.toArray(new String[allKeys.size()]);
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
	@Override
	public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems)
	{
		Collection<PhpClass> phpClasses = PhpFullFqClassIndex.INSTANCE.get(name, project, GlobalSearchScope.allScope(project));
		return phpClasses.toArray(new NavigationItem[phpClasses.size()]);
	}
}
