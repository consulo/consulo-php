package consulo.php.lang.psi;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author jay
 * @date Jun 24, 2008 9:20:14 PM
 */
public interface PhpImplementsList extends PhpElement
{

	@Nonnull
	public List<PhpClass> getInterfaces();

}
