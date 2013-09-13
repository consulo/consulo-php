package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 15, 2008 12:34:38 PM
 */
public interface FunctionCall extends PHPPsiElement, PsiPolyVariantReference
{

	boolean canReadName();

	String getFunctionName();

	PhpParameterList getParameterList();
}
