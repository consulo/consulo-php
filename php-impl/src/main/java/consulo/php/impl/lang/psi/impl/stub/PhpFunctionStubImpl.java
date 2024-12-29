package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.stubs.PhpFunctionStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.StubElement;
import consulo.php.impl.lang.psi.PhpStubElements;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFunctionStubImpl extends PhpNamedStubImpl<Function> implements PhpFunctionStub
{
	public PhpFunctionStubImpl(StubElement parent, @Nullable StringRef name, short flags)
	{
		super(parent, PhpStubElements.FUNCTION, name, flags);
	}

	public PhpFunctionStubImpl(StubElement parent, @Nullable String name, short flags)
	{
		super(parent, PhpStubElements.FUNCTION, name, flags);
	}

	@Nonnull
	@Override
	public Collection<String> getDocExceptions()
	{
		return Collections.emptyList();
	}
}
