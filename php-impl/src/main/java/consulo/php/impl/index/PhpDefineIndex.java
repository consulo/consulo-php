package consulo.php.impl.index;

import com.jetbrains.php.lang.psi.elements.PhpDefine;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.psi.stub.StringStubIndexExtension;
import consulo.language.psi.stub.StubIndexKey;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
@ExtensionImpl
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
