package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.UnaryExpression;
import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:09:42 PM
 */
public interface PhpUnaryExpression extends UnaryExpression
{
	/**
	 * @deprecated use {@link #getValue()}
	 */
	@Deprecated
	default PsiElement getExpression()
	{
		return getValue();
	}
}
