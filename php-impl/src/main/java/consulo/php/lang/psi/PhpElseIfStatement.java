package consulo.php.lang.psi;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:22 AM
 */
public interface PhpElseIfStatement extends PhpElement
{

	public PhpElement getCondition();

	public PhpElement getStatement();

}
