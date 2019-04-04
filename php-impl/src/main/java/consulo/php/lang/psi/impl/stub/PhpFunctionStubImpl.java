package consulo.php.lang.psi.impl.stub;

import java.util.Collection;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.stubs.PhpFunctionStub;
import consulo.php.lang.psi.PhpStubElements;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFunctionStubImpl extends PhpNamedStubImpl<Function> implements PhpFunctionStub
{
	public PhpFunctionStubImpl(StubElement parent, @Nullable StringRef name)
	{
		super(parent, PhpStubElements.FUNCTION, name);
	}

	public PhpFunctionStubImpl(StubElement parent, @Nullable String name)
	{
		super(parent, PhpStubElements.FUNCTION, name);
	}

	@Override
	public Collection<String> getDocExceptions()
	{
		return null;
	}
}
