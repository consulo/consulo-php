package org.consulo.php.lang.psi.elements;

import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;

import javax.swing.*;

/**
 * @author jay
 * @date Jun 4, 2008 11:40:27 AM
 */
public interface PhpNamedElement extends PsiNameIdentifierOwner, PsiNamedElement
{
	PhpDocComment getDocComment();

	Icon getIcon();
}
