package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
@ExtensionImpl
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
