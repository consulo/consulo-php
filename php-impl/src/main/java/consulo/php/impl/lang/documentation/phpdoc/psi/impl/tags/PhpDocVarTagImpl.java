package consulo.php.impl.lang.documentation.phpdoc.psi.impl.tags;

import javax.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.impl.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Jun 29, 2008 2:10:52 AM
 */
public class PhpDocVarTagImpl extends PhpDocPsiElementImpl implements PhpDocVarTag
{

	public PhpDocVarTagImpl(ASTNode node)
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
		final PhpDocType type = PsiTreeUtil.getChildOfType(this, PhpDocType.class);
		if(type != null)
		{
			return type.getType();
		}
		return null;
	}

}
