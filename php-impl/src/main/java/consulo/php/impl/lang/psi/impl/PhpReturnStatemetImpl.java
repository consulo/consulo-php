package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
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
		// the returned expression, null for a plain return
		return PsiTreeUtil.getChildOfType(this, PhpPsiElement.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpReturn(this);
	}
}
