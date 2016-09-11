package consulo.php.lang.documentation.phpdoc.psi.impl.tags;

import consulo.php.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.lang.documentation.phpdoc.psi.impl.PhpDocPsiElementImpl;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;

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
	public void accept(@NotNull PhpElementVisitor visitor)
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
