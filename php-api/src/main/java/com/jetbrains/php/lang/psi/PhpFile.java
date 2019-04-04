package com.jetbrains.php.lang.psi;

import javax.annotation.Nonnull;

import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpFile extends PsiFile, PhpPsiElement
{
	@Nonnull
	PhpPsiElement[] getTopLevelElements();
}
