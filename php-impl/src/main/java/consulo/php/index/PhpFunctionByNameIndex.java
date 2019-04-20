package consulo.php.index;

import javax.annotation.Nonnull;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.jetbrains.php.lang.psi.elements.Function;

/**
 * @author VISTALL
 * @since 2019-04-20
 */
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
