package consulo.php.lang.psi.resolve;

import java.util.ArrayList;
import java.util.List;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:34:48 PM
 */
public class PhpVariantsProcessor extends PhpScopeProcessor
{

	private List<PhpPsiElement> variants = new ArrayList<PhpPsiElement>();

	public PhpVariantsProcessor(PhpPsiElement element)
	{
		super(element);
	}

	public List<PhpPsiElement> getVariants()
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
				variants.add((PhpPsiElement) element);
			}
		}
		return true;
	}
}
