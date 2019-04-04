package consulo.php.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date May 9, 2008 3:21:12 PM
 */
public interface PhpIfStatement extends PhpPsiElement
{

	public PhpPsiElement getCondition();

	public PhpElseIfStatement[] getElseIfBranches();

	public PhpElseStatement getElseBranch();

	public PhpPsiElement getStatement();

}
