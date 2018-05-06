package consulo.php.index;

import consulo.php.lang.psi.PhpClass;
import javax.annotation.Nonnull;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFullFqClassIndex extends StringStubIndexExtension<PhpClass>
{
	public static PhpFullFqClassIndex INSTANCE = new PhpFullFqClassIndex();

	@Nonnull
	@Override
	public StubIndexKey<String, PhpClass> getKey()
	{
		return PhpIndexKeys.FULL_FQ_CLASSES;
	}
}
