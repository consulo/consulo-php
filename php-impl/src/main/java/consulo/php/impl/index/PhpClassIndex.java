package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
@ExtensionImpl
public class PhpClassIndex extends StringStubIndexExtension<PhpClass>
{
	public static PhpClassIndex INSTANCE = new PhpClassIndex();

	@Nonnull
	@Override
	public StubIndexKey<String, PhpClass> getKey()
	{
		return PhpIndexKeys.CLASSES;
	}
}
