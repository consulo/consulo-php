package com.jetbrains.php.lang.psi.elements;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @date Apr 15, 2008 1:59:56 PM
 */
public interface ParameterList extends PhpPsiElement
{
	@Nonnull
	Parameter[] getParameters();
}
