package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;

/**
 * @author jay
 * @date May 5, 2008 8:06:33 AM
 */
public interface PhpGlobal extends PhpPsiElement
{

	public Variable[] getVariables();

}
