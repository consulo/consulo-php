package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.resolve.types.PhpType;
import consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import consulo.php.lang.psi.resolve.types.PhpTypeOwner;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 18, 2008 12:32:13 PM
 */
public class PhpTypeOwnerImpl extends PhpElementImpl implements PhpTypeOwner
{

	public PhpTypeOwnerImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	@Nonnull
	public PhpType getType()
	{
		PhpType type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		if(type == null)
		{
			PhpTypeAnnotatorVisitor.process(this);
		}
		type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		assert type != null;
		return type;
	}
}
