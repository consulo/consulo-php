package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import consulo.language.psi.PsiElement;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:13 AM
 */
public interface PhpAssignmentExpression extends PhpPsiElement, PhpExpression, PhpTypedElement
{

	public PsiElement getVariable();

	public PsiElement getValue();

}
