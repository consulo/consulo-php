package consulo.php.lang.psi;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;

/**
 * @author jay
 * @date Apr 15, 2008 4:07:54 PM
 */
public interface PhpCatchStatement extends PhpPsiElement
{

	public Variable getException();

	public ClassReference getExceptionType();

	public PsiElement getStatement();

}
