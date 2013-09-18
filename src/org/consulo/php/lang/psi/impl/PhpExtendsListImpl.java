package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpClassReference;
import org.consulo.php.lang.psi.PhpExtendsList;
import org.consulo.php.lang.psi.PhpClass;

import org.jetbrains.annotations.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Jun 7, 2008 7:07:36 PM
 */
public class PhpExtendsListImpl extends PhpElementImpl implements PhpExtendsList
{
	public PhpExtendsListImpl(ASTNode node)
	{
		super(node);
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
			if(element instanceof PhpClassReference)
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
