package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.lang.psi.PhpElvisExpression;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;

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
	public PhpType getType()
	{
		PhpPsiElement firstPsiChild = getFirstPsiChild();
		if(firstPsiChild instanceof PhpReference)
		{
			PhpType type = ((PhpReference) firstPsiChild).resolveLocalType();
			if(type != PhpType.EMPTY)
			{
				return type;
			}
		}

		PhpExpression expression = findChildByClass(PhpExpression.class);
		if(expression != null)
		{
			return expression.getType();
		}

		return PhpType.EMPTY;
	}
}
