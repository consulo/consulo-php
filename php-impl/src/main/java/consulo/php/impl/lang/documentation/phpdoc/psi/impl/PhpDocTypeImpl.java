package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import javax.annotation.Nonnull;

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
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	public String getType()
	{
		return getText();
	}

}
