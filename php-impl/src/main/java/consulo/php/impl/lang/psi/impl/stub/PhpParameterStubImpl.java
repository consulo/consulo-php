package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubElement;
import consulo.util.lang.BitUtil;

import jakarta.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-04-10
 */
public class PhpParameterStubImpl extends PhpNamedStubImpl<Parameter> implements PhpParameterStub
{
	public static final int VARIADIC = 1 << 0;
	public static final int OPTIONAL = 1 << 1;
	public static final int PASS_BY_REF = 1 << 2;

	public PhpParameterStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	public PhpParameterStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	@Override
	public boolean isPassByRef()
	{
		return BitUtil.isSet(myFlags, PASS_BY_REF);
	}

	@Override
	public boolean isOptional()
	{
		return BitUtil.isSet(myFlags, OPTIONAL);
	}

	@Override
	public boolean isVariadic()
	{
		return BitUtil.isSet(myFlags, VARIADIC);
	}

	@Nullable
	@Override
	public String getDefaultValuePresentation()
	{
		return null;
	}
}
