package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpForeachStatement;
import consulo.php.lang.psi.PhpVariableReference;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

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
	public PhpVariableReference getKey()
	{
		PsiElement[] children = getChildren();
		int variables = 0;
		PhpVariableReference firstVariable = null;
		for(int i = 1; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child instanceof PhpVariableReference)
			{
				if(++variables == 1)
				{
					firstVariable = (PhpVariableReference) child;
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
	public PhpVariableReference getValue()
	{
		PsiElement lastChild = getLastChild();
		while(lastChild != null)
		{
			if(lastChild instanceof PhpVariableReference)
			{
				return (PhpVariableReference) lastChild;
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