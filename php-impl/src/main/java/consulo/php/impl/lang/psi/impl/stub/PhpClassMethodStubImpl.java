package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.stubs.PhpMethodStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubElement;
import consulo.util.lang.BitUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 2019-03-30
 */
public class PhpClassMethodStubImpl extends PhpMemberStubImpl<Method> implements PhpMethodStub
{
	public static short packFlags(Method method)
	{
		int flags = 0;
		PhpModifier.Access access = method.getAccess();
		flags = BitUtil.set(flags, PRIVATE, access.isPrivate());
		flags = BitUtil.set(flags, PROTECTED, access.isProtected());

		flags = BitUtil.set(flags, STATIC, method.isStatic());
		flags = BitUtil.set(flags, FINAL, method.isFinal());
		flags = BitUtil.set(flags, ABSTRACT, method.isAbstract());

		flags = BitUtil.set(flags, DEPRECATED, method.isDeprecated());
		flags = BitUtil.set(flags, INTERNAL, method.isInternal());
		return (short) flags;
	}

	public PhpClassMethodStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	public PhpClassMethodStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	@Nonnull
	@Override
	public Collection<String> getDocExceptions()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isReturningByReference()
	{
		return false;
	}
}
