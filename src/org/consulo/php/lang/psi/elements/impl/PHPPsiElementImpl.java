package org.consulo.php.lang.psi.elements.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.lang.psi.elements.PhpElement;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 13.03.2007
 *
 * @author jay
 */
public class PHPPsiElementImpl extends ASTWrapperPsiElement implements PhpElement
{

	public PHPPsiElementImpl(ASTNode node)
	{
		super(node);
	}

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

	public void accept(@NotNull final PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpElement(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement source)
	{
		return processor.execute(this, state);
	}

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
