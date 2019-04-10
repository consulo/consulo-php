package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 18, 2008 1:34:34 AM
 */
public class PhpNewExpressionImpl extends PhpTypedElementImpl implements NewExpression
{
	public PhpNewExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitNewExpression(this);
	}

	@Nullable
	@Override
	public ClassReference getClassReference()
	{
		return findChildByClass(ClassReference.class);
	}

	@Nullable
	@Override
	public ParameterList getParameterList()
	{
		return findChildByClass(ParameterList.class);
	}

	@Nonnull
	@Override
	public PsiElement[] getParameters()
	{
		ParameterList parameterList = getParameterList();
		return parameterList == null ? PsiElement.EMPTY_ARRAY : parameterList.getParameters();
	}
}
