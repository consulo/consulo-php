package consulo.php.lang.psi;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpUseStatement extends PhpElement
{
	@Nonnull
	PhpClassReference[] getClassReferences();
}
