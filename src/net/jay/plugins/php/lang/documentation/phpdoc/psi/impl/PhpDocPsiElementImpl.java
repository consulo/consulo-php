package net.jay.plugins.php.lang.documentation.phpdoc.psi.impl;

import net.jay.plugins.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;
import net.jay.plugins.php.lang.psi.elements.impl.PHPPsiElementImpl;

import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 29, 2008 2:09:01 AM
 */
abstract public class PhpDocPsiElementImpl extends PHPPsiElementImpl implements PhpDocPsiElement
{

	public PhpDocPsiElementImpl(ASTNode node)
	{
		super(node);
	}

}
