package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.PhpModifier;
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
	public static final int PROMOTED = 1 << 3;
	public static final int ACCESS_PROTECTED = 1 << 4;
	public static final int ACCESS_PRIVATE = 1 << 5;

	public static int accessFlags(PhpModifier.Access access)
	{
		return access == PhpModifier.Access.PRIVATE ? ACCESS_PRIVATE : access == PhpModifier.Access.PROTECTED ? ACCESS_PROTECTED : 0;
	}

	public static PhpModifier.Access getAccess(int flags)
	{
		if(BitUtil.isSet(flags, ACCESS_PRIVATE))
		{
			return PhpModifier.Access.PRIVATE;
		}
		return BitUtil.isSet(flags, ACCESS_PROTECTED) ? PhpModifier.Access.PROTECTED : PhpModifier.Access.PUBLIC;
	}

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
