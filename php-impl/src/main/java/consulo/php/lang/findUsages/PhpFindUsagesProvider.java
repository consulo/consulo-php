package consulo.php.lang.findUsages;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpNamedElement;
import consulo.php.lang.psi.PhpParameter;
import consulo.php.lang.psi.PhpVariableReference;

import javax.annotation.Nullable;
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

	@Override
	@Nullable
	public WordsScanner getWordsScanner()
	{
		if(wordsScanner == null)
		{
			wordsScanner = new PhpWordsScanner();
		}
		return wordsScanner;
	}

	@Override
	public boolean canFindUsagesFor(@Nonnull PsiElement psiElement)
	{
		return psiElement instanceof PhpClass || psiElement instanceof PhpFunction || psiElement instanceof PhpField || psiElement instanceof PhpParameter || psiElement instanceof PhpVariableReference;
	}

	@Override
	@Nullable
	public String getHelpId(@Nonnull PsiElement psiElement)
	{
		return null;
	}

	@Override
	@Nonnull
	public String getType(@Nonnull PsiElement element)
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
		if(element instanceof PhpFunction)
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
	@Override
	@Nonnull
	public String getDescriptiveName(@Nonnull PsiElement element)
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
	@Override
	@Nonnull
	public String getNodeText(@Nonnull PsiElement element, boolean useFullName)
	{
		if(element instanceof PhpNamedElement)
		{
			final String name = ((PhpNamedElement) element).getName();
			return name == null ? "" : name;
		}
		return "";
	}
}
