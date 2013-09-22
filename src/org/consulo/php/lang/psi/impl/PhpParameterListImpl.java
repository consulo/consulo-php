package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpParameter;
import org.consulo.php.lang.psi.PhpParameterList;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 2:01:07 PM
 */
public class PhpParameterListImpl extends PhpElementImpl implements PhpParameterList
{
	public PhpParameterListImpl(ASTNode node)
	{
		super(node);
	}

	@NotNull
	@Override
	public PhpParameter[] getParameters()
	{
		return findChildrenByClass(PhpParameter.class);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitParameterList(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(lastParent == null)
		{
			for(PsiElement parameter : getParameters())
			{
				if(!parameter.processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
