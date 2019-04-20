package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public abstract class PhpStubbedElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T>, PhpPsiElement
{
	public PhpStubbedElementImpl(@Nonnull T stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PhpStubbedElementImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getParent()
	{
		return getParentByStub();
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
	public PhpPsiElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpPsiElement)
			{
				return (PhpPsiElement) children[0];
			}
		}
		return null;
	}

	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpPsiElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && children.length > i + 1)
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

	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpPsiElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && i > 0)
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
