package consulo.php.impl.lang.psi.impl;

import java.util.List;
import org.jspecify.annotations.Nullable;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpElvisExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpElvisExpressionImpl extends PhpElementImpl implements PhpElvisExpression
{
	public PhpElvisExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitElvisExpression(this);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public PhpType getType()
	{
		return PhpTypeInference.getType(this);
	}

	@Nullable
	@Override
	public PhpPsiElement getCondition()
	{
		return PhpPsiChildren.phpChild(this, 0);
	}

	@Nullable
	@Override
	public PhpPsiElement getTrueVariant()
	{
		return getCondition();
	}

	@Nullable
	@Override
	public PhpPsiElement getFalseVariant()
	{
		List<PhpPsiElement> children = PhpPsiChildren.phpChildren(this);
		return children.size() < 2 ? null : children.get(children.size() - 1);
	}

	@Override
	public boolean isShort()
	{
		return true;
	}
}
