package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.PhpTryStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:01:36 AM
 */
public class PhpTryStatementImpl extends PhpElementImpl implements PhpTryStatement
{

	public PhpTryStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpPsiElement getStatement()
	{
		return getFirstPsiChild();
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitTryStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState resolveState, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(lastParent == null)
		{
			if(!getStatement().processDeclarations(processor, resolveState, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, resolveState, lastParent, source);
	}

}
