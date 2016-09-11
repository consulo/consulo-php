package consulo.php.lang.psi.impl;

import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpTryStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

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
	public PhpElement getStatement()
	{
		return getFirstPsiChild();
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitTryStatement(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState resolveState, PsiElement lastParent, @NotNull PsiElement source)
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
