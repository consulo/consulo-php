package org.consulo.php.lang.findUsages;

import net.jay.plugins.php.lang.psi.elements.*;
import org.consulo.php.lang.psi.elements.PhpField;

import org.consulo.php.lang.psi.elements.PhpNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Jul 1, 2008 12:24:11 AM
 */
public class PhpFindUsagesProvider implements FindUsagesProvider
{
	private PhpWordsScanner wordsScanner;

	@Nullable
	public WordsScanner getWordsScanner()
	{
		if(wordsScanner == null)
			wordsScanner = new PhpWordsScanner();
		return wordsScanner;
	}

	public boolean canFindUsagesFor(@NotNull PsiElement psiElement)
	{
		return psiElement instanceof PhpClass || psiElement instanceof PhpMethod || psiElement instanceof Function || psiElement instanceof PhpField || psiElement instanceof PhpParameter || psiElement instanceof PhpVariableReference;
	}

	@Nullable
	public String getHelpId(@NotNull PsiElement psiElement)
	{
		return null;
	}

	@NotNull
	public String getType(@NotNull PsiElement element)
	{
		if(element instanceof PhpVariableReference)
		{
			return "PhpVariableReference";
		}
		if(element instanceof PhpParameter)
		{
			return "PhpParameter";
		}
		if(element instanceof PhpClass)
		{
			return ((PhpClass) element).isInterface() ? "Interface" : "Class";
		}

		if(element instanceof PhpMethod)
		{
			return "PhpMethod";
		}
		if(element instanceof Function)
		{
			return "Function";
		}
		if(element instanceof PhpField)
		{
			return "PhpField";
		}
		return "";
	}

	/**
	 * Returns an expanded user-visible name of the specified element, shown in the "Find Usages"
	 * dialog. For classes, this can return a fully qualified name of the class; for methods -
	 * a signature of the method with parameters.
	 *
	 * @param element the element for which the name is requested.
	 * @return the user-visible name.
	 */
	@NotNull
	public String getDescriptiveName(@NotNull PsiElement element)
	{
		if(element instanceof PhpNamedElement)
		{
			final String name = ((PhpNamedElement) element).getName();
			return name == null ? "" : name;
		}
		return "";
	}

	/**
	 * Returns the text representing the specified PSI element in the Find Usages tree.
	 *
	 * @param element     the element for which the node text is requested.
	 * @param useFullName if true, the returned text should use fully qualified names
	 * @return the text representing the element.
	 */
	@NotNull
	public String getNodeText(@NotNull PsiElement element, boolean useFullName)
	{
		if(element instanceof PhpNamedElement)
		{
			final String name = ((PhpNamedElement) element).getName();
			return name == null ? "" : name;
		}
		return "";
	}
}
