package org.consulo.php.lang.documentation.phpdoc.psi.impl;

import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocType;

import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 29, 2008 2:20:02 AM
 */
public class PhpDocTypeImpl extends PhpDocPsiElementImpl implements PhpDocType
{

	public PhpDocTypeImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public String getType()
	{
		return getText();
	}

}
