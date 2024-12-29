package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpFunctionParameterListImpl extends PhpElementImpl implements ParameterList
{
	public PhpFunctionParameterListImpl(ASTNode node)
	{
		super(node);
	}

	@Nonnull
	@Override
	public PsiElement[] getParameters()
	{
		return findChildrenByClass(Parameter.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitParameterList(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
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

