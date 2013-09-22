package org.consulo.php.lang.psi.resolve;

import java.util.ArrayList;
import java.util.List;

import org.consulo.php.lang.psi.PhpElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:34:48 PM
 */
public class PhpVariantsProcessor extends PhpScopeProcessor
{

	private List<PhpElement> variants = new ArrayList<PhpElement>();

	public PhpVariantsProcessor(PhpElement element)
	{
		super(element);
	}

	public List<PhpElement> getVariants()
	{
		return variants;
	}

	@Override
	public boolean execute(PsiElement element)
	{
		if(element instanceof PsiNamedElement)
		{
			if(isAppropriateDeclarationType(element))
			{
				variants.add((PhpElement) element);
			}
		}
		return true;
	}
}
