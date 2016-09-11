package consulo.php.lang.psi.impl;

import consulo.php.lang.parser.PhpElementTypes;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpGroupStatement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
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
	public final void accept(@NotNull final PsiElementVisitor visitor)
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

	@Override
	public PhpElement getFirstPsiChild()
	{
		return PhpElementImpl.getFirstPsiChild(this);
	}

	@Override
	public PhpElement getNextPsiSibling()
	{
		return PhpElementImpl.getNextPsiSibling(this);
	}

	@Override
	public PhpElement getPrevPsiSibling()
	{
		return PhpElementImpl.getPrevPsiSibling(this);
	}
}
