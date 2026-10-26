package consulo.php.impl.lang.psi.impl;

import java.util.List;
import org.jspecify.annotations.Nullable;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpTernaryExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpTernaryExpressionImpl extends PhpElementImpl implements PhpTernaryExpression
{
	public PhpTernaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitTernaryExpression(this);
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
		return PhpPsiChildren.phpChildren(this).size() < 3 ? null : PhpPsiChildren.phpChild(this, 1);
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
		return false;
	}
}
