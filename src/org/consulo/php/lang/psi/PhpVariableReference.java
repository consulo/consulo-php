package org.consulo.php.lang.psi;

import org.consulo.php.lang.psi.resolve.types.PhpTypeOwner;
import com.intellij.psi.PsiPolyVariantReference;

/**
 * @author jay
 * @date Apr 3, 2008 9:55:25 PM
 */
public interface PhpVariableReference extends PhpElement, PhpNamedElement, PsiPolyVariantReference, PhpTypeOwner
{
	public boolean canReadName();

	public boolean isDeclaration();
}
