package consulo.php.lang.psi;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @date Apr 15, 2008 1:59:56 PM
 */
public interface PhpParameterList extends PhpElement
{
	@Nonnull
	PhpParameter[] getParameters();
}
