package org.consulo.php.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * @author jay
 * @date Apr 3, 2008 11:12:27 PM
 */
public interface PhpBinaryExpression extends PhpElement
{

	public PsiElement getLeftOperand();

	public PsiElement getRightOperand();

	public IElementType getOperation();

}
