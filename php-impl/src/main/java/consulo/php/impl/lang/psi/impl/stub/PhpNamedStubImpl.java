package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpNamedStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.NamedStubBase;
import consulo.language.psi.stub.StubElement;
import consulo.util.lang.BitUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-04-05
 */
public class PhpNamedStubImpl<T extends PhpNamedElement> extends NamedStubBase<T> implements PhpNamedStub<T>
{
	public static final short DEPRECATED = 1 << 13;
	public static final short INTERNAL = 1 << 14;

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
	public final short getFlags()
	{
		return myFlags;
	}

	@Override
	public final boolean isDeprecated()
	{
		return BitUtil.isSet(myFlags, DEPRECATED);
	}

	@Override
	public final boolean isInternal()
	{
		return BitUtil.isSet(myFlags, INTERNAL);
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return PhpType.EMPTY;
	}
}
