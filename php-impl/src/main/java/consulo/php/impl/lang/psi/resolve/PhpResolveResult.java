package consulo.php.impl.lang.psi.resolve;

import consulo.language.psi.PsiElementResolveResult;
import consulo.language.psi.PsiElement;

/**
 * @author jay
 * @date Jun 6, 2008 4:16:07 PM
 */
public class PhpResolveResult extends PsiElementResolveResult
{
	public PhpResolveResult(PsiElement element)
	{
		super(element, true);
	}
}
