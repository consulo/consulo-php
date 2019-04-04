package consulo.php.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date Jul 2, 2008 3:21:58 AM
 */
public interface PhpForStatement extends PhpPsiElement
{

	public PhpPsiElement getInitialExpression();

	public PhpPsiElement getConditionalExpression();

	public PhpPsiElement getRepeatedExpression();

	public PhpPsiElement getStatement();

}
