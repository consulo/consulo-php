package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.psi.scope.GlobalSearchScope;

import java.util.Collection;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpIndexUtil
{
	public static Collection<PhpClass> getClassesForName(PhpPsiElement element, String name)
	{
		return PhpFullFqClassIndex.INSTANCE.get(name, element.getProject(), GlobalSearchScope.allScope(element.getProject()));
		//return Collections.emptyList();
	}
}
