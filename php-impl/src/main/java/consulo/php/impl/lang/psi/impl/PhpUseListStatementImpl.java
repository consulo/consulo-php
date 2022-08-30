package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpUseListStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public class PhpUseListStatementImpl extends PhpElementImpl implements PhpUseListStatement
{
	public PhpUseListStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpUse[] getUses()
	{
		return findChildrenByClass(PhpUse.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUseList(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		for(PhpUse use : getUses())
		{
			if(!use.processDeclarations(processor, state, lastParent, source))
			{
				return false;
			}
		}
		return true;
	}
}
