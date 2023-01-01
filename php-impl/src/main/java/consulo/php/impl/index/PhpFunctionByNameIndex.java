package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.Function;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-20
 */
@ExtensionImpl
public class PhpFunctionByNameIndex extends StringStubIndexExtension<Function>
{
	public static PhpFunctionByNameIndex INSTANCE = new PhpFunctionByNameIndex();

	@Nonnull
	@Override
	public StubIndexKey<String, Function> getKey()
	{
		return PhpIndexKeys.FUNCTION_BY_NAME;
	}
}
