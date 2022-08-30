package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import consulo.language.ast.ASTNode;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;
import consulo.php.impl.lang.psi.impl.PhpElementImpl;

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
