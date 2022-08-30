package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:22 AM
 */
public interface PhpElseIfStatement extends PhpPsiElement
{

	public PhpPsiElement getCondition();

	public PhpPsiElement getStatement();

}
