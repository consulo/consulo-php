package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.php.lang.psi.PhpUseListStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

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
