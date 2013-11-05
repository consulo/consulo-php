package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
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
public abstract class PhpElementImpl extends ASTWrapperPsiElement implements PhpElement
{
	public PhpElementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	@NotNull
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}

	@Override
	public final void accept(@NotNull final PsiElementVisitor visitor)
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

	public abstract void accept(@NotNull final PhpElementVisitor visitor);

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		return processor.execute(this, state);
	}

	@Override
	public PhpElement getFirstPsiChild()
	{
		return getFirstPsiChild(this);
	}

	@Override
	public PhpElement getNextPsiSibling()
	{
		return getNextPsiSibling(this);
	}

	@Override
	public PhpElement getPrevPsiSibling()
	{
		return getPrevPsiSibling(this);
	}

	public static PhpElement getFirstPsiChild(PsiElement element)
	{
		PsiElement[] children = element.getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpElement)
			{
				return (PhpElement) children[0];
			}
		}
		return null;
	}

	public static PhpElement getNextPsiSibling(PsiElement element)
	{
		PsiElement[] children = element.getParent().getChildren();
		PhpElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == element && children.length > i + 1)
			{
				if(children[i + 1] instanceof PhpElement)
				{
					nextSibling = (PhpElement) children[i + 1];
				}
				break;
			}
		}
		return nextSibling;
	}

	public static PhpElement getPrevPsiSibling(PsiElement element)
	{
		PsiElement[] children = element.getParent().getChildren();
		PhpElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == element && i > 0)
			{
				if(children[i - 1] instanceof PhpElement)
				{
					prevSibling = (PhpElement) children[i - 1];
				}
				break;
			}
		}
		return prevSibling;
	}
}
