package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import consulo.annotation.access.RequiredReadAction;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

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
