package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.BitUtil;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.stubs.PhpMemberStub;

/**
 * @author VISTALL
 * @since 2019-04-05
 */
public class PhpMemberStubImpl<T extends PhpClassMember> extends PhpNamedStubImpl<T> implements PhpMemberStub<T>
{
	// see DEPRECATED - its 13
	public static final int STATIC = 1 << 12;
	public static final int FINAL = 1 << 11;
	public static final int ABSTRACT = 1 << 10;
	public static final int PRIVATE = 1 << 9;
	public static final int PROTECTED = 1 << 8;

	public PhpMemberStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	public PhpMemberStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	@Override
	public boolean isStatic()
	{
		return BitUtil.isSet(myFlags, STATIC);
	}

	@Override
	public boolean isFinal()
	{
		return BitUtil.isSet(myFlags, FINAL);
	}

	@Override
	public boolean isAbstract()
	{
		return BitUtil.isSet(myFlags, ABSTRACT);
	}

	@Override
	public boolean isPrivate()
	{
		return BitUtil.isSet(myFlags, PRIVATE);
	}

	@Override
	public boolean isProtected()
	{
		return BitUtil.isSet(myFlags, PROTECTED);
	}
}
