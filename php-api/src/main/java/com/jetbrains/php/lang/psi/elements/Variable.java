package com.jetbrains.php.lang.psi.elements;

import jakarta.annotation.Nonnull;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.util.lang.function.Condition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jay
 * @date Apr 3, 2008 9:55:25 PM
 */
public interface Variable extends PhpNamedElement, PhpReference
{
	String THIS = "this";
	/**
	 * Name of {@code $this}: variable and parameter names are without the {@code $}
	 */
	String $THIS = "this";

	Set<String> SUPERGLOBALS = new HashSet<>(Arrays.asList(
			"GLOBALS",
			"_COOKIE",
			"_ENV",
			//"HTTP_ENV_VARS",
			"_FILES",
			//"HTTP_POST_FILES",
			"_GET",
			//"HTTP_GET_VARS",
			"_POST",
			//"HTTP_POST_VARS",
			"_REQUEST",
			"_SERVER",
			//"HTTP_SERVER_VARS",
			"_SESSION",
			//"HTTP_SESSION_VARS",
			//"argc",
			//"argv",
			//"HTTP_RAW_POST_DATA",
			"http_response_header",
			"php_errormsg"
	));
	Condition<PsiElement> INSTANCEOF = use -> use instanceof Variable;

	public boolean canReadName();

	public boolean isDeclaration();

	// both PhpNamedElement and PhpReference declare these

	@Override
	String getName();

	@Nonnull
	@Override
	PhpType getType();

	@Nonnull
	@Override
	default CharSequence getNameCS()
	{
		return PhpNamedElement.super.getNameCS();
	}

	@Nonnull
	@Override
	String getNamespaceName();

	@Override
	default String getFQN()
	{
		return PhpNamedElement.super.getFQN();
	}
}
