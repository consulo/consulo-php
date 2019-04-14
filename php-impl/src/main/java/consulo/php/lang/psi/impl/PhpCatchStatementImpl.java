package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.Catch;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Apr 15, 2008 4:08:31 PM
 */
public class PhpCatchStatementImpl extends PhpElementImpl implements Catch
{
	public PhpCatchStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public ClassReference getExceptionType()
	{
		return PsiTreeUtil.getChildOfType(this, ClassReference.class);
	}

	@Override
	public Variable getException()
	{
		return PsiTreeUtil.getChildOfType(this, Variable.class);
	}

	@Override
	public PsiElement getStatement()
	{
		final Variable exception = getException();
		if(exception != null)
		{
			return exception.getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitCatchStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(lastParent == null || lastParent == getStatement())
		{
			if(getException() != null && !getException().processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}
}
