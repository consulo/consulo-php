package org.consulo.php.lang.psi.elements;

/**
 * @author jay
 * @date Jul 2, 2008 3:06:22 AM
 */
public interface ElseIf extends PhpElement
{

	public PhpElement getCondition();

	public PhpElement getStatement();

}
