package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.psi.PhpGroupStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date May 9, 2008 5:12:53 PM
 */
public class PhpGroupStatementImpl extends LazyParseablePsiElement implements PhpGroupStatement
{
	public PhpGroupStatementImpl(CharSequence buffer)
	{
		super(PhpElementTypes.GROUP_STATEMENT, buffer);
	}

	@Override
	public final void accept(@Nonnull final PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) visitor).visitPhpElement(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	@Nonnull
	public PsiElement[] getStatements()
	{
		return getChildren();
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
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
		else if(lastParent instanceof PhpPsiElement)
		{
			PhpPsiElement statement = ((PhpPsiElement) lastParent).getPrevPsiSibling();
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

	@Override
	public PhpPsiElement getFirstPsiChild()
	{
		return PhpElementImpl.getFirstPsiChild(this);
	}

	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		return PhpElementImpl.getNextPsiSibling(this);
	}

	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		return PhpElementImpl.getPrevPsiSibling(this);
	}
}
