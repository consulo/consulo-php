package consulo.php.lang.psi.resolve;

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:34:48 PM
 */
public class PhpVariantsProcessor extends PhpScopeProcessor
{
	private List<PhpNamedElement> variants = new ArrayList<>();

	public PhpVariantsProcessor(PhpPsiElement element)
	{
		super(element);
	}

	public List<PhpNamedElement> getVariants()
	{
		return variants;
	}

	@Override
	public boolean execute(PsiElement element)
	{
		if(element instanceof PhpNamedElement)
		{
			if(isAppropriateDeclarationType(element))
			{
				variants.add((PhpNamedElement) element);
			}
		}
		return true;
	}
}
