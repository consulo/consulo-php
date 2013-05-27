package net.jay.plugins.php.lang.psi.resolve;

import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:34:48 PM
 */
public class PhpVariantsProcessor extends PhpScopeProcessor
{

	private List<PHPPsiElement> variants = new ArrayList<PHPPsiElement>();

	public PhpVariantsProcessor(PsiNamedElement element)
	{
		super(element);
	}

	public List<PHPPsiElement> getVariants()
	{
		return variants;
	}

	public boolean execute(PsiElement element)
	{
		if(element instanceof PsiNamedElement)
		{
			if(isAppropriateDeclarationType(element))
			{
				variants.add((PHPPsiElement) element);
			}
		}
		return true;
	}
}
