package com.jetbrains.php.lang.psi.elements;

import javax.annotation.Nullable;

/**
 * @author jay
 * @date Jun 7, 2008 7:03:18 PM
 */
public interface ExtendsList extends PhpPsiElement
{

	@Nullable
	public PhpClass getExtendsClass();

}
