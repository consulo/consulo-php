package org.consulo.php.lang.psi.elements;

/**
 * @author jay
 * @date Jul 2, 2008 3:21:58 AM
 */
public interface For extends PhpElement
{

	public PhpElement getInitialExpression();

	public PhpElement getConditionalExpression();

	public PhpElement getRepeatedExpression();

	public PhpElement getStatement();

}
