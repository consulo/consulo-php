package org.consulo.php.lang.psi.elements;

/**
 * @author jay
 * @date May 9, 2008 3:21:12 PM
 */
public interface If extends PhpElement
{

	public PhpElement getCondition();

	public ElseIf[] getElseIfBranches();

	public Else getElseBranch();

	public PhpElement getStatement();

}
