package com.jetbrains.php.lang.psi.elements;

import java.util.List;

import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface ImplementsList extends PhpPsiElement
{
	@Nonnull
	public List<PhpClass> getInterfaces();

	/**
	 * @return the listed classes
	 */
	default List<ClassReference> getReferenceElements()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, ClassReference.class);
	}
}
