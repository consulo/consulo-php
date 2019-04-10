package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.BitUtil;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpNamedStub;

/**
 * @author VISTALL
 * @since 2019-04-05
 */
public class PhpNamedStubImpl<T extends PhpNamedElement> extends NamedStubBase<T> implements PhpNamedStub<T>
{
	public static final short DEPRECATED = 1 << 0;
	public static final short INTERNAL = 1 << 1;

	protected final short myFlags;

	public PhpNamedStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name);
		myFlags = flags;
	}

	public PhpNamedStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name);
		myFlags = flags;
	}

	@Override
	public short getFlags()
	{
		return myFlags;
	}

	@Override
	public boolean isDeprecated()
	{
		return BitUtil.isSet(myFlags, DEPRECATED);
	}

	@Override
	public boolean isInternal()
	{
		return BitUtil.isSet(myFlags, INTERNAL);
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpType.VOID;
	}
}
