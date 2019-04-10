package consulo.php.lang.psi.impl.stub;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;
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
