package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.stubs.PhpMemberStub;

/**
 * @author VISTALL
 * @since 2019-04-05
 */
public class PhpMemberStubImpl<T extends PhpClassMember> extends PhpNamedStubImpl<T> implements PhpMemberStub<T>
{
	public PhpMemberStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name)
	{
		super(parent, elementType, name);
	}

	public PhpMemberStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name)
	{
		super(parent, elementType, name);
	}

	@Override
	public boolean isStatic()
	{
		return false;
	}

	@Override
	public boolean isFinal()
	{
		return false;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public boolean isPrivate()
	{
		return false;
	}

	@Override
	public boolean isProtected()
	{
		return false;
	}
}
