package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.PhpUseListStatement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date May 9, 2008 5:12:53 PM
 */
public class PhpGroupStatementImpl extends PhpElementImpl implements GroupStatement
{
	public PhpGroupStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
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
			PsiElement[] statements = getStatements();
			for(PsiElement statement : statements)
			{
				if(statement instanceof PhpUseListStatement)
				{
					if(!statement.processDeclarations(processor, state, lastParent, source))
					{
						return false;
					}
				}
			}

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
}
