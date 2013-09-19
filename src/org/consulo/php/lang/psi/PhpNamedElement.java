package org.consulo.php.lang.psi;

import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;

/**
 * @author jay
 * @date Jun 4, 2008 11:40:27 AM
 */
public interface PhpNamedElement extends PsiNameIdentifierOwner, PsiNamedElement, PhpElement
{
	PhpDocComment getDocComment();
}
