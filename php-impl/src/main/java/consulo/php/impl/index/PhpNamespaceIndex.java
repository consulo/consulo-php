package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-14
 */
@ExtensionImpl
public class PhpNamespaceIndex extends StringStubIndexExtension<PhpNamespace>
{
	public static PhpNamespaceIndex INSTANCE = new PhpNamespaceIndex();

	@Nonnull
	@Override
	public StubIndexKey<String, PhpNamespace> getKey()
	{
		return PhpIndexKeys.NAMESPACES;
	}
}

