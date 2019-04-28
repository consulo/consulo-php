package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 15, 2008 2:01:07 PM
 */
public class PhpExpressionParameterListImpl extends PhpElementImpl implements ParameterList
{
	public PhpExpressionParameterListImpl(ASTNode node)
	{
		super(node);
	}

	@Nonnull
	@Override
	public PsiElement[] getParameters()
	{
		return findChildrenByClass(PhpPsiElement.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitParameterList(this);
	}
}
