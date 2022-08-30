package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

import javax.annotation.Nonnull;

/**
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public abstract class PhpElementImpl extends ASTWrapperPsiElement implements PhpPsiElement
{
	public PhpElementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public final void accept(@Nonnull final PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			accept((PhpElementVisitor) visitor);
		}
		else
		{
			super.accept(visitor);
		}
	}

	public abstract void accept(@Nonnull final PhpElementVisitor visitor);

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		return processor.execute(this, state);
	}

	@Override
	public PhpPsiElement getFirstPsiChild()
	{
		return getFirstPsiChild(this);
	}

	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		return getNextPsiSibling(this);
	}

	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		return getPrevPsiSibling(this);
	}

	public static PhpPsiElement getFirstPsiChild(PsiElement element)
	{
		PsiElement[] children = element.getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpPsiElement)
			{
				return (PhpPsiElement) children[0];
			}
		}
		return null;
	}

	public static PhpPsiElement getNextPsiSibling(PsiElement element)
	{
		PsiElement[] children = element.getParent().getChildren();
		PhpPsiElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == element && children.length > i + 1)
			{
				if(children[i + 1] instanceof PhpPsiElement)
				{
					nextSibling = (PhpPsiElement) children[i + 1];
				}
				break;
			}
		}
		return nextSibling;
	}

	public static PhpPsiElement getPrevPsiSibling(PsiElement element)
	{
		PsiElement[] children = element.getParent().getChildren();
		PhpPsiElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == element && i > 0)
			{
				if(children[i - 1] instanceof PhpPsiElement)
				{
					prevSibling = (PhpPsiElement) children[i - 1];
				}
				break;
			}
		}
		return prevSibling;
	}
}
