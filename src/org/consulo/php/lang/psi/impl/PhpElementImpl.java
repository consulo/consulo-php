package org.consulo.php.lang.psi.impl;

import javax.swing.Icon;

import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

	@Nullable
	public Icon getIcon()
	{
		return null;
	}

	@Nullable
	public Icon getIcon(int i)
	{
		Icon icon = getIcon();
		if(icon != null)
		{
			return icon;
		}
		return null;
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
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpElement)
			{
				return (PhpElement) children[0];
			}
		}
		return null;
	}

	@Override
	public PhpElement getNextPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && children.length > i + 1)
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

	@Override
	public PhpElement getPrevPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && i > 0)
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
