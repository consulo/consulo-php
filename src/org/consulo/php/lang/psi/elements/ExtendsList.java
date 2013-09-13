package org.consulo.php.lang.psi.elements;

import org.jetbrains.annotations.Nullable;

/**
 * @author jay
 * @date Jun 7, 2008 7:03:18 PM
 */
public interface ExtendsList extends PHPPsiElement
{

	@Nullable
	public PhpClass getExtendsClass();

}
