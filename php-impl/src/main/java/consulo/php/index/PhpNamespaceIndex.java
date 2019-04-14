package consulo.php.index;

import javax.annotation.Nonnull;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;

/**
 * @author VISTALL
 * @since 2019-04-14
 */
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

