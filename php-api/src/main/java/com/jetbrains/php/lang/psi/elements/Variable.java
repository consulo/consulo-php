package com.jetbrains.php.lang.psi.elements;

import gnu.trove.THashSet;

import java.util.Arrays;
import java.util.Set;

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date Apr 3, 2008 9:55:25 PM
 */
public interface Variable extends PhpPsiElement, PhpNamedElement, PsiPolyVariantReference, PhpTypedElement
{
	String THIS = "this";
	String $THIS = "$this";

	Set<String> SUPERGLOBALS = new THashSet<>(Arrays.asList(
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
}
