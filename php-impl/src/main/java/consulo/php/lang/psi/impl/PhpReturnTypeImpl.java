package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpReturnType;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-05
 */
public class PhpReturnTypeImpl extends PhpElementImpl implements PhpReturnType
{
	public PhpReturnTypeImpl(ASTNode node)
	{
		super(node);
	}

	@RequiredReadAction
	@Override
	public ClassReference getClassReference()
	{
		return findChildByClass(ClassReference.class);
	}

	@RequiredReadAction
	@Override
	public boolean isNullable()
	{
		return findChildByType(PhpTokenTypes.opQUEST) != null;
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpType.VOID;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitReturnType(this);
	}
}