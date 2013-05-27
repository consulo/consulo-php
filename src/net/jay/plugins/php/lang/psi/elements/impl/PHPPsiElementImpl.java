package net.jay.plugins.php.lang.psi.elements.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.psi.elements.LightCopyContainer;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;

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
public class PHPPsiElementImpl extends ASTWrapperPsiElement implements PHPPsiElement
{

	public PHPPsiElementImpl(ASTNode node)
	{
		super(node);
	}

	public String toString()
	{
		return getNode().getElementType().toString();
	}

	@NotNull
	public Language getLanguage()
	{
		return PHPFileType.PHP.getLanguage();
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

	public List<LightCopyContainer> getChildrenForCache()
	{
		List<LightCopyContainer> elements = new ArrayList<LightCopyContainer>();
		for(PsiElement element : getChildren())
		{
			if(element instanceof LightCopyContainer)
			{
				elements.add((LightCopyContainer) element);
			}
			else if(element instanceof PHPPsiElement)
			{
				elements.addAll(((PHPPsiElement) element).getChildrenForCache());
			}
		}
		return elements;
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

	public PHPPsiElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PHPPsiElement)
			{
				return (PHPPsiElement) children[0];
			}
		}
		return null;
	}

	public PHPPsiElement getNextPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PHPPsiElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && children.length > i + 1)
			{
				if(children[i + 1] instanceof PHPPsiElement)
				{
					nextSibling = (PHPPsiElement) children[i + 1];
				}
				break;
			}
		}
		return nextSibling;
	}

	public PHPPsiElement getPrevPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PHPPsiElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && i > 0)
			{
				if(children[i - 1] instanceof PHPPsiElement)
				{
					prevSibling = (PHPPsiElement) children[i - 1];
				}
				break;
			}
		}
		return prevSibling;
	}
}
