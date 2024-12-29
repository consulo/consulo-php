package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.ast.ASTNode;

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

	@Nonnull
	@Override
	public PhpType getType()
	{
		ClassReference classReference = getClassReference();
		if(classReference != null)
		{
			return classReference.resolveLocalType();
		}
		return PhpType.EMPTY;
	}
}
