package consulo.php.index;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassIndex extends StringStubIndexExtension<PhpClass>
{
	public static PhpFullFqClassIndex INSTANCE = new PhpFullFqClassIndex();

	@Nonnull
	@Override
	public StubIndexKey<String, PhpClass> getKey()
	{
		return PhpIndexKeys.CLASSES;
	}
}
