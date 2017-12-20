package consulo.php.index;

import consulo.php.lang.psi.PhpClass;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFullFqClassIndex extends StringStubIndexExtension<PhpClass>
{
	public static PhpFullFqClassIndex INSTANCE = new PhpFullFqClassIndex();

	@NotNull
	@Override
	public StubIndexKey<String, PhpClass> getKey()
	{
		return PhpIndexKeys.FULL_FQ_CLASSES;
	}
}
