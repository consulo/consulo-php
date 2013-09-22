package org.consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpClassReference;
import org.consulo.php.lang.psi.PhpImplementsList;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Jun 24, 2008 9:21:14 PM
 */
public class PhpImplementsListImpl extends PhpElementImpl implements PhpImplementsList
{
	public PhpImplementsListImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Override
	@NotNull
	public List<PhpClass> getInterfaces()
	{
		List<PhpClass> result = new ArrayList<PhpClass>();

		final PsiElement[] children = getChildren();
		for(PsiElement child : children)
		{
			if(child instanceof PhpClassReference)
			{
				//noinspection ConstantConditions
				final PsiElement element = child.getReference().resolve();
				if(element instanceof PhpClass)
				{
					result.add((PhpClass) element);
				}
			}
		}

		return result;
	}
}
