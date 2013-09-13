package org.consulo.php.lang.documentation.phpdoc.psi.impl;

import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;
import org.consulo.php.lang.psi.elements.impl.PhpElementImpl;

import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 29, 2008 2:09:01 AM
 */
abstract public class PhpDocPsiElementImpl extends PhpElementImpl implements PhpDocPsiElement
{

	public PhpDocPsiElementImpl(ASTNode node)
	{
		super(node);
	}

}
