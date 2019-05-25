package consulo.php.lang.psi.impl.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.BitUtil;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;

import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpFieldStubImpl extends PhpMemberStubImpl<Field> implements PhpFieldStub
{
	private static final int CONSTANT = 1 << 1;

	public static short packFlags(Field field)
	{
		int flags = 0;

		flags = BitUtil.set(flags, CONSTANT, field.isConstant());
		flags = BitUtil.set(flags, DEPRECATED, field.isDeprecated());
		flags = BitUtil.set(flags, INTERNAL, field.isInternal());
		return (short) flags;
	}

	public PhpFieldStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	public PhpFieldStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	@Override
	public boolean isConstant()
	{
		return BitUtil.isSet(getFlags(), CONSTANT);
	}

	@Override
	public String getDefaultValuePresentation()
	{
		return null;
	}
}
