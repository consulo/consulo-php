package consulo.php.impl.lang.psi;

import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.lang.psi.elements.PhpUseList;
import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-03-11
 */
public interface PhpUseListStatement extends PhpUseList
{
	@Nonnull
	default PhpUse[] getUses()
	{
		return getDeclarations();
	}
}
