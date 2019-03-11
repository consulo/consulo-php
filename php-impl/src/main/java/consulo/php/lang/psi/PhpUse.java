package consulo.php.lang.psi;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public interface PhpUse extends PhpElement
{
	@Nonnull
	PhpClassReference[] getClassReferences();
}
