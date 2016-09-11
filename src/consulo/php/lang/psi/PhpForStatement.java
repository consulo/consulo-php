package consulo.php.lang.psi;

/**
 * @author jay
 * @date Jul 2, 2008 3:21:58 AM
 */
public interface PhpForStatement extends PhpElement
{

	public PhpElement getInitialExpression();

	public PhpElement getConditionalExpression();

	public PhpElement getRepeatedExpression();

	public PhpElement getStatement();

}
