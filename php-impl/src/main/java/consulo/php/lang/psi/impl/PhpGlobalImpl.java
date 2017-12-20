package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import consulo.php.lang.psi.PhpGlobal;
import consulo.php.lang.psi.PhpVariableReference;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * @author jay
 * @date May 5, 2008 8:06:55 AM
 */
public class PhpGlobalImpl extends PhpElementImpl implements PhpGlobal
{
	public PhpGlobalImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	public PhpVariableReference[] getVariables()
	{
		List<PhpVariableReference> variables = new ArrayList<PhpVariableReference>();
		for(PsiElement element : this.getChildren())
		{
			if(element instanceof PhpVariableReference)
			{
				variables.add((PhpVariableReference) element);
			}
		}
		return variables.toArray(new PhpVariableReference[variables.size()]);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		for(PhpVariableReference variable : this.getVariables())
		{
			if(!processor.execute(variable, state))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
