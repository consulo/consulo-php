package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.PhpUse;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public interface PhpUseListStatement extends PhpPsiElement
{
	PhpUse[] getUses();
}
