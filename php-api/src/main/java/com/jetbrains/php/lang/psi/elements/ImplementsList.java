package com.jetbrains.php.lang.psi.elements;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface ImplementsList extends PhpPsiElement
{
	@Nonnull
	public List<PhpClass> getInterfaces();
}
