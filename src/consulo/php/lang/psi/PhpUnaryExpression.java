package consulo.php.lang.psi;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:09:42 PM
 */
public interface PhpUnaryExpression extends PhpElement
{

	public PsiElement getExpression();

}
