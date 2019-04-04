package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
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
	public PhpNamedStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name)
	{
		super(parent, elementType, name);
	}

	public PhpNamedStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name)
	{
		super(parent, elementType, name);
	}

	@Override
	public short getFlags()
	{
		return 0;
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Override
	public boolean isInternal()
	{
		return false;
	}

	@Override
	public PhpType getType()
	{
		return null;
	}
}
