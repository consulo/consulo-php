package org.consulo.php.lang.psi.elements;

import org.consulo.php.lang.psi.resolve.types.PhpTypedElement;

import com.intellij.psi.PsiElement;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:13 AM
 */
public interface AssignmentExpression extends PhpElement, PhpTypedElement
{

	public PsiElement getVariable();

	public PsiElement getValue();

}
