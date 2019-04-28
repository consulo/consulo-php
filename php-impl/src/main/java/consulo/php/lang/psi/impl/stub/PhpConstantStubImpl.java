package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.stubs.PhpConstantStub;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpConstantStubImpl extends PhpNamedStubImpl<Constant> implements PhpConstantStub
{
	public PhpConstantStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	public PhpConstantStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	@Override
	public boolean isCaseSensitive()
	{
		return false;
	}

	@Override
	public String getNamespaceName()
	{
		return null;
	}

	@Override
	public String getValuePresentation()
	{
		return null;
	}
}
