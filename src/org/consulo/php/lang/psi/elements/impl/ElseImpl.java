package org.consulo.php.lang.psi.elements.impl;

import org.consulo.php.lang.psi.elements.Else;
import org.consulo.php.lang.psi.elements.PHPPsiElement;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Jul 2, 2008 3:12:20 AM
 */
public class ElseImpl extends PHPPsiElementImpl implements Else
{

	public ElseImpl(ASTNode node)
	{
		super(node);
	}

	public PHPPsiElement getStatement()
	{
		return getFirstPsiChild();
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpElse(this);
		}
		else
		{
			visitor.visitElement(this);
		}
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
