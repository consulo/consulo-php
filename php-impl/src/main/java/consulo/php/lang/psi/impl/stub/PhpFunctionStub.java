package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.PhpStubElements;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFunctionStub extends NamedStubBase<PhpFunction>
{
	public PhpFunctionStub(StubElement parent, @Nullable StringRef name)
	{
		super(parent, PhpStubElements.FUNCTION, name);
	}

	public PhpFunctionStub(StubElement parent, @Nullable String name)
	{
		super(parent, PhpStubElements.FUNCTION, name);
	}
}
