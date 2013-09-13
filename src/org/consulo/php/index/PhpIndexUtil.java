package org.consulo.php.index;

import com.intellij.psi.search.GlobalSearchScope;
import org.consulo.php.lang.psi.elements.PhpElement;
import org.consulo.php.lang.psi.elements.PhpClass;
import org.consulo.php.lang.psi.elements.PhpMethod;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpIndexUtil {

	public static Collection<PhpClass> getClassesFor(PhpElement element) {
		return getClassesForName(element, null);
		//return Collections.emptyList();
	}

	public static Collection<PhpClass> getClassesForName(PhpElement element, String name) {
		return PhpClassIndex.INSTANCE.get(name, element.getProject(), GlobalSearchScope.allScope(element.getProject()));
		//return Collections.emptyList();
	}

	public static Collection<PhpMethod> getMethodsFor(PhpElement element) {
		return Collections.emptyList();
	}
}
