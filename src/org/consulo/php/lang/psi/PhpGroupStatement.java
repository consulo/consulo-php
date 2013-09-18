package org.consulo.php.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface PhpGroupStatement extends PhpElement
{
	@NotNull
	PsiElement[] getStatements();
}
