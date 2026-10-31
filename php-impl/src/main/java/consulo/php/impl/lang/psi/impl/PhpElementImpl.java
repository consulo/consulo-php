package consulo.php.impl.lang.psi.impl;

import consulo.annotation.access.RequiredReadAction;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.php.impl.lang.psi.resolve.types.PhpTypeInference;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

import jakarta.annotation.Nonnull;

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

	/**
	 * The next sibling which is a PHP element - error elements and other nodes between are skipped
	 */
	public static PhpPsiElement getNextPsiSibling(PsiElement element)
	{
		for(PsiElement sibling = element.getNextSibling(); sibling != null; sibling = sibling.getNextSibling())
		{
			if(sibling instanceof PhpPsiElement phpSibling)
			{
				return phpSibling;
			}
		}
		return null;
	}

	/**
	 * The previous sibling which is a PHP element - error elements and other nodes between are skipped
	 */
	public static PhpPsiElement getPrevPsiSibling(PsiElement element)
	{
		for(PsiElement sibling = element.getPrevSibling(); sibling != null; sibling = sibling.getPrevSibling())
		{
			if(sibling instanceof PhpPsiElement phpSibling)
			{
				return phpSibling;
			}
		}
		return null;
	}

	/**
	 * Local type of this expression, see {@link PhpTypeInference}
	 */
	@Nonnull
	@RequiredReadAction
	public PhpType getType()
	{
		return PhpTypeInference.getType(this);
	}
}
