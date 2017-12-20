package consulo.php.lang.psi;

/**
 * @author jay
 * @date May 9, 2008 3:21:12 PM
 */
public interface PhpIfStatement extends PhpElement
{

	public PhpElement getCondition();

	public PhpElseIfStatement[] getElseIfBranches();

	public PhpElseStatement getElseBranch();

	public PhpElement getStatement();

}
