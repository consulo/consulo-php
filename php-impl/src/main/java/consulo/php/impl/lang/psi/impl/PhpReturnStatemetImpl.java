package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpReturn;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-13
 */
public class PhpReturnStatemetImpl extends PhpElementImpl implements PhpReturn
{
	public PhpReturnStatemetImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getArgument()
	{
		return null;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpReturn(this);
	}
}
