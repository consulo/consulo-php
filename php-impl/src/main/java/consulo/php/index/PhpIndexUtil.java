package consulo.php.index;

import java.util.Collection;
import java.util.Collections;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Function;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpIndexUtil
{

	public static Collection<PhpClass> getClassesFor(PhpPsiElement element)
	{
		return getClassesForName(element, null);
		//return Collections.emptyList();
	}

	public static Collection<PhpClass> getClassesForName(PhpPsiElement element, String name)
	{
		return PhpFullFqClassIndex.INSTANCE.get(name, element.getProject(), GlobalSearchScope.allScope(element.getProject()));
		//return Collections.emptyList();
	}

	public static Collection<Function> getMethodsFor(PhpPsiElement element)
	{
		return Collections.emptyList();
	}
}
