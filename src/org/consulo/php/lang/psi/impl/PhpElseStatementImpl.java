package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpElseStatement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

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
	public PhpElement getStatement()
	{
		return getFirstPsiChild();
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitElseStatement(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
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
