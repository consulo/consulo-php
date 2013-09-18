package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpWhileStatement;

import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date May 18, 2008 9:29:28 PM
 */
public class PhpWhileStatementImpl extends PhpElementImpl implements PhpWhileStatement
{
	public PhpWhileStatementImpl(ASTNode node)
	{
		super(node);
	}
}
