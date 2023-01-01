package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.php.impl.lang.psi.PhpElseStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;

/**
 * @author jay
 * @date Jul 2, 2008 3:12:20 AM
 */
public class PhpElseStatementImpl extends PhpElementImpl implements PhpElseStatement
{

	public PhpElseStatementImpl(ASTNode node)
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
		visitor.visitElseStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(lastParent == null)
		{
			if(!getStatement().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}
}
