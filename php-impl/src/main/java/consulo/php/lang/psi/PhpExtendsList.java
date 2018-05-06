package consulo.php.lang.psi;

import javax.annotation.Nullable;

/**
 * @author jay
 * @date Jun 7, 2008 7:03:18 PM
 */
public interface PhpExtendsList extends PhpElement
{

	@Nullable
	public PhpClass getExtendsClass();

}
