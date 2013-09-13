package org.consulo.php.lang.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.consulo.php.lang.psi.elements.PhpClassReference;
import org.consulo.php.lang.psi.elements.ImplementsList;
import org.consulo.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @date Jun 24, 2008 9:21:14 PM
 */
public class ImplementsListImpl extends PHPPsiElementImpl implements ImplementsList
{
	public ImplementsListImpl(ASTNode node)
	{
		super(node);
	}

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
