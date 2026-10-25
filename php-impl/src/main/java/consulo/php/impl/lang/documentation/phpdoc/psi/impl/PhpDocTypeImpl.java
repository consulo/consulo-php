package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import jakarta.annotation.Nonnull;

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
	public String getName()
	{
		return getText();
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpTypeInference.getDocType(this);
	}

}
