package com.jetbrains.php.lang.psi.elements;

import consulo.annotation.access.RequiredReadAction;

import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author jay
 * @date Jun 7, 2008 7:03:18 PM
 */
public interface ExtendsList extends PhpPsiElement
{
	@Nullable
	@RequiredReadAction
	PhpClass getExtendsClass();

	/**
	 * @return the listed classes
	 */
	default List<ClassReference> getReferenceElements()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, ClassReference.class);
	}
}
