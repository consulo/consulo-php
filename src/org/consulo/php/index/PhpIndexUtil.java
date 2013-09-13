package org.consulo.php.index;

import com.intellij.psi.search.GlobalSearchScope;
import org.consulo.php.lang.psi.elements.PHPPsiElement;
import org.consulo.php.lang.psi.elements.PhpClass;
import org.consulo.php.lang.psi.elements.PhpMethod;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpIndexUtil {

	public static Collection<PhpClass> getClassesFor(PHPPsiElement element) {
		return getClassesForName(element, null);
		//return Collections.emptyList();
	}

	public static Collection<PhpClass> getClassesForName(PHPPsiElement element, String name) {
		return PhpClassIndex.INSTANCE.get(name, element.getProject(), GlobalSearchScope.allScope(element.getProject()));
		//return Collections.emptyList();
	}

	public static Collection<PhpMethod> getMethodsFor(PHPPsiElement element) {
		return Collections.emptyList();
	}
}
