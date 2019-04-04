package consulo.php.lang.psi.impl.stub;

import java.util.Collection;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.stubs.PhpMethodStub;

/**
 * @author VISTALL
 * @since 2019-03-30
 */
public class PhpClassMethodStubImpl extends PhpMemberStubImpl<Method> implements PhpMethodStub
{
	public PhpClassMethodStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name)
	{
		super(parent, elementType, name);
	}

	public PhpClassMethodStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name)
	{
		super(parent, elementType, name);
	}

	@Override
	public Collection<String> getDocExceptions()
	{
		return null;
	}

	@Override
	public boolean isReturningByReference()
	{
		return false;
	}
}
