package com.jetbrains.php.lang.psi.elements;

import org.jspecify.annotations.Nullable;

/**
 * @author VISTALL
 * @since 2019-04-12
 */
public interface ArrayAccessExpression extends PhpExpression
{
	/**
	 * @return the accessed array: {@code $a} of {@code $a['x']}
	 */
	@Nullable
	PhpPsiElement getValue();

	/**
	 * @return {@code ['x']} of {@code $a['x']}
	 */
	@Nullable
	ArrayIndex getIndex();
}
