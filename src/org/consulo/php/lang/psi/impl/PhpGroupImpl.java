package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpGroup;
import org.consulo.php.lang.psi.PhpStubElements;
import org.consulo.php.lang.psi.impl.stub.PhpGroupStub;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date May 9, 2008 5:12:53 PM
 */
public class PhpGroupImpl extends PhpStubbedElementImpl<PhpGroupStub> implements PhpGroup
{
	public PhpGroupImpl(ASTNode node)
	{
		super(node);
	}

	public PhpGroupImpl(@NotNull PhpGroupStub stub)
	{
		super(stub, PhpStubElements.GROUP);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	@NotNull
	public PsiElement[] getStatements()
	{
		return getChildren();
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		if(lastParent == null)
		{
			for(PsiElement statement : getStatements())
			{
				if(!statement.processDeclarations(processor, state, null, source))
				{
					return false;
				}
			}
		}
		else if(lastParent instanceof PhpElement)
		{
			PhpElement statement = ((PhpElement) lastParent).getPrevPsiSibling();
			while(statement != null)
			{
				if(!statement.processDeclarations(processor, state, null, source))
				{
					return false;
				}
				statement = statement.getPrevPsiSibling();
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
