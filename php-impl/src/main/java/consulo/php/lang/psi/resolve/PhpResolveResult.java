package consulo.php.lang.psi.resolve;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;

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
