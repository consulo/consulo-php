package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;

/**
 * Created by IntelliJ IDEA.
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
	@Nonnull
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
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
