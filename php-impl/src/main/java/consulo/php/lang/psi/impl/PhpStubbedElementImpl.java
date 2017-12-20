package consulo.php.lang.psi.impl;

import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
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
public abstract class PhpStubbedElementImpl<T extends StubElement> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T>, PhpElement
{
	public PhpStubbedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PhpStubbedElementImpl(@NotNull ASTNode node)
	{
		super(node);
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
