package org.consulo.php.lang.psi;

import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public interface PhpBraceOwner extends PhpElement
{
	PsiElement getLeftBrace();

	PsiElement getRightBrace();
}
