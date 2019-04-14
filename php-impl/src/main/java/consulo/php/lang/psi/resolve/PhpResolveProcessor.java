package consulo.php.lang.psi.resolve;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.jetbrains.php.lang.psi.elements.Catch;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;

/**
 * @author jay
 * @date Apr 15, 2008 10:10:23 AM
 */
public class PhpResolveProcessor extends PhpScopeProcessor
{
	public static enum ElementKind
	{
		FIELD,
		PARAMETER,
		FUNCTION,
		NONE
	}

	private Set<PsiElement> result = new LinkedHashSet<>();
	private Set<ElementKind> myKinds;
	private String myName;

	public PhpResolveProcessor(PhpPsiElement element, String name, ElementKind... kinds)
	{
		super(element);
		myName = name;
		myKinds = EnumSet.copyOf(Arrays.asList(kinds));
	}

	public Collection<PsiElement> getResult()
	{
		return result;
	}

	@Override
	public boolean execute(PsiElement psiElement)
	{
		if(psiElement == element)
		{
			return true;
		}

		ElementKind kind = getKind(psiElement);
		if(!myKinds.contains(kind))
		{
			return true;
		}

		String name = ((PsiNamedElement) psiElement).getName();
		if(Comparing.equal(name, myName))
		{
			result.add(psiElement);
		}
		return true;
	}

	public static ElementKind getKind(PsiElement element)
	{
		if(element instanceof Field)
		{
			return ElementKind.FIELD;
		}

		if(element instanceof Parameter)
		{
			return ElementKind.PARAMETER;
		}

		if(element instanceof Function)
		{
			return ElementKind.FUNCTION;
		}

		if(element instanceof Variable && element.getParent() instanceof Catch)
		{
			return ElementKind.PARAMETER;
		}

		return ElementKind.NONE;
	}
}
