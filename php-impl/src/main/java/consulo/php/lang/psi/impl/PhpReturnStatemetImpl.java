package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpReturn;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

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
