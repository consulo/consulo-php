package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpReturnType;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

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

	@Nonnull
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
		ClassReference classReference = getClassReference();
		return classReference.resolveLocalType();
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitReturnType(this);
	}
}
