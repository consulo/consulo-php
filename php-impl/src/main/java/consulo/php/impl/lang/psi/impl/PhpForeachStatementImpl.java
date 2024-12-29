package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpForeachStatement;
import com.jetbrains.php.lang.psi.elements.Variable;
import consulo.language.ast.ASTNode;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 3:36:30 PM
 */
public class PhpForeachStatementImpl extends PhpElementImpl implements PhpForeachStatement
{
	public PhpForeachStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getArray()
	{
		return getFirstPsiChild();
	}

	@Override
	public Variable getKey()
	{
		PsiElement[] children = getChildren();
		int variables = 0;
		Variable firstVariable = null;
		for(int i = 1; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child instanceof Variable)
			{
				if(++variables == 1)
				{
					firstVariable = (Variable) child;
				}
			}
		}
		if(variables == 2)
		{
			return firstVariable;
		}
		return null;
	}

	@Override
	public Variable getValue()
	{
		PsiElement lastChild = getLastChild();
		while(lastChild != null)
		{
			if(lastChild instanceof Variable)
			{
				return (Variable) lastChild;
			}
			lastChild = lastChild.getPrevSibling();
		}
		return null;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitForeachStatement(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		if(getKey() != null && lastParent != getArray() && !getKey().processDeclarations(processor, state, null, source))
		{
			return false;
		}
		if(getValue() != null && lastParent != getArray() && !getValue().processDeclarations(processor, state, null, source))
		{
			return false;
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
