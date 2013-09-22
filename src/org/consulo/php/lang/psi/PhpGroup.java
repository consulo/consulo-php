package org.consulo.php.lang.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date May 9, 2008 5:12:18 PM
 */
public interface PhpGroup extends PhpElement
{
	@NotNull
	PsiElement[] getStatements();
}
