package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.resolve.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpUseImpl extends PhpElementImpl implements PhpUse
{
	public PhpUseImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUse(this);
	}

	@Nonnull
	@Override
	public ClassReference[] getClassReferences()
	{
		return findChildrenByClass(ClassReference.class);
	}

	@Nullable
	@Override
	public String getAliasName()
	{
		return null;
	}

	@Override
	@RequiredReadAction
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		ClassReference[] classReferences = getClassReferences();
		for(ClassReference classReference : classReferences)
		{
			PsiElement element = classReference.resolve();
			if(element == null)
			{
				continue;
			}

			if(!processor.execute(element, state))
			{
				return false;
			}
		}
		return true;
	}
}
