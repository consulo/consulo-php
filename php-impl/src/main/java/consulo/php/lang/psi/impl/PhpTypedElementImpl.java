package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Jun 18, 2008 12:32:13 PM
 */
public class PhpTypedElementImpl extends PhpElementImpl implements PhpTypedElement
{

	public PhpTypedElementImpl(ASTNode node)
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
