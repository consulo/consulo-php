package consulo.php.lang.psi.resolve.types;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @date Jun 17, 2008 2:24:52 PM
 */
public interface PhpTypeOwner
{

	@Nonnull
	public PhpType getType();

}
