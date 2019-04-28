package consulo.php.index;

import javax.annotation.Nonnull;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.jetbrains.php.lang.psi.elements.PhpDefine;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpDefineIndex extends StringStubIndexExtension<PhpDefine>
{
	public static final PhpDefineIndex INSTANCE = new PhpDefineIndex();

	@Nonnull
	@Override
	public StubIndexKey<String, PhpDefine> getKey()
	{
		return PhpIndexKeys.DEFINES;
	}
}
