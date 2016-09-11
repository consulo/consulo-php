package consulo.php.lang.psi.resolve;

import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;

/**
 * @author jay
 * @date Jun 6, 2008 4:16:07 PM
 */
public class PhpResolveResult implements ResolveResult
{

	private PsiElement element;

	public PhpResolveResult(PsiElement element)
	{
		this.element = element;
	}

	/**
	 * Returns the result of the resolve.
	 *
	 * @return an element the reference is resolved to.
	 */
	@Override
	@Nullable
	public PsiElement getElement()
	{
		return element;
	}

	/**
	 * Checks if the reference was resolved to a valid element.
	 *
	 * @return true if the resolve encountered no problems
	 */
	@Override
	public boolean isValidResult()
	{
		return element != null && element.isValid();

	}
}
