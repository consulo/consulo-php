package consulo.php.lang.psi.impl;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-03-15
 */
public class PhpArrayCreationExpressionImpl extends PhpElementImpl implements ArrayCreationExpression
{
	public PhpArrayCreationExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitArrayExpression(this);
	}

	@Override
	public boolean isShortSyntax()
	{
		return findChildrenByClass(ArrayHashElement.class).length == 0;
	}

	@Nonnull
	@Override
	public Iterable<ArrayHashElement> getHashElements()
	{
		return Arrays.asList(findChildrenByClass(ArrayHashElement.class));
	}
}
