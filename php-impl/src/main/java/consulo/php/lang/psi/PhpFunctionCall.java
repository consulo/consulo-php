package consulo.php.lang.psi;

import com.intellij.psi.PsiPolyVariantReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author jay
 * @date May 15, 2008 12:34:38 PM
 */
public interface PhpFunctionCall extends PhpPsiElement, PsiPolyVariantReference
{

	boolean canReadName();

	String getFunctionName();

	ParameterList getParameterList();
}
