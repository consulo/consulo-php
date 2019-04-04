package consulo.php.lang.psi.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ExtendsList;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Jun 7, 2008 7:07:36 PM
 */
public class PhpExtendsListImpl extends PhpElementImpl implements ExtendsList
{
	public PhpExtendsListImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	@Nullable
	public PhpClass getExtendsClass()
	{
		final PsiElement[] children = getChildren();
		assert children.length <= 1;
		if(children.length > 0)
		{
			final PsiElement element = children[0];
			if(element instanceof ClassReference)
			{
				//noinspection ConstantConditions
				final PsiElement resolveResult = element.getReference().resolve();
				if(resolveResult instanceof PhpClass)
				{
					return (PhpClass) resolveResult;
				}
			}
		}
		return null;
	}
}
