package consulo.php.lang.psi;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date Apr 15, 2008 9:09:42 PM
 */
public interface PhpUnaryExpression extends PhpPsiElement
{

	public PsiElement getExpression();

}
