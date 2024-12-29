package com.jetbrains.php.lang.psi.elements;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.resolve.types.PhpType;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpExpression extends PhpPsiElement, PhpTypedElement
{
	@Nonnull
	@Override
	default PhpType getType()
	{
		// TODO [VISTALL] remove after resolver finish
		return PhpType.EMPTY;
	}
}
