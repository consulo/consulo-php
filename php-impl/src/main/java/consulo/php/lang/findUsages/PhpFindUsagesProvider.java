package consulo.php.lang.findUsages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.php.lang.psi.elements.*;
import consulo.php.PhpLanguageLevel;
import consulo.php.lang.lexer.PhpFlexLexer;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpTokenSets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author jay
 * @date Jul 1, 2008 12:24:11 AM
 */
public class PhpFindUsagesProvider implements FindUsagesProvider
{
	@Override
	@Nullable
	public WordsScanner getWordsScanner()
	{
		return new DefaultWordsScanner(new PhpFlexLexer(false, PhpLanguageLevel.HIGHEST), TokenSet.create(PhpTokenTypes.VARIABLE, PhpTokenSets.IDENTIFIER), PhpTokenSets.tsCOMMENTS, PhpTokenSets
				.tsSTRINGS);
	}

	@Override
	public boolean canFindUsagesFor(@Nonnull PsiElement psiElement)
	{
		return psiElement instanceof PhpNamedElement;
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
		if(element instanceof Variable)
		{
			return "variable";
		}
		if(element instanceof Parameter)
		{
			return "parameter";
		}
		if(element instanceof PhpNamespace)
		{
			return "namespace";
		}
		if(element instanceof Constant)
		{
			return "constant";
		}
		if(element instanceof PhpClass)
		{
			if(((PhpClass) element).isInterface())
			{
				return "interface";
			}
			else if(((PhpClass) element).isTrait())
			{
				return "trait";
			}
			else
			{
				return "class";
			}
		}
		if(element instanceof Function)
		{
			return "function";
		}
		if(element instanceof Field)
		{
			return "field";
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
			return ((PhpNamedElement) element).getName();
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
			return ((PhpNamedElement) element).getName();
		}
		return "";
	}
}
