package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpCatchStatement;
import org.consulo.php.lang.psi.PhpClassReference;
import org.consulo.php.lang.psi.PhpVariableReference;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Apr 15, 2008 4:08:31 PM
 */
public class PhpCatchStatementImpl extends PhpElementImpl implements PhpCatchStatement
{

	public PhpCatchStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PhpClassReference getExceptionType()
	{
		return PsiTreeUtil.getChildOfType(this, PhpClassReference.class);
	}

	@Override
	public PhpVariableReference getException()
	{
		return PsiTreeUtil.getChildOfType(this, PhpVariableReference.class);
	}

	@Override
	public PsiElement getStatement()
	{
		final PhpVariableReference exception = getException();
		if(exception != null)
		{
			return exception.getNextPsiSibling();
		}
		return null;
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitCatchStatement(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
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
