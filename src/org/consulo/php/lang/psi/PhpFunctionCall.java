package org.consulo.php.lang.psi;

import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date May 15, 2008 12:34:38 PM
 */
public interface PhpFunctionCall extends PhpElement, PsiPolyVariantReference
{

	boolean canReadName();

	String getFunctionName();

	PhpParameterList getParameterList();
}
