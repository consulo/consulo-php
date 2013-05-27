package net.jay.plugins.php.lang.psi.elements.impl;

import net.jay.plugins.php.lang.psi.elements.While;

import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date May 18, 2008 9:29:28 PM
 */
public class WhileImpl extends PHPPsiElementImpl implements While
{
	public WhileImpl(ASTNode node)
	{
		super(node);
	}
}
