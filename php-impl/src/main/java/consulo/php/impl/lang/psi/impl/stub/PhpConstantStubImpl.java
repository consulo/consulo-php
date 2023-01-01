package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.Constant;
import com.jetbrains.php.lang.psi.stubs.PhpConstantStub;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubElement;

import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-04-29
 */
public class PhpConstantStubImpl extends PhpNamedStubImpl<Constant> implements PhpConstantStub
{
	private final String myValuePresentation;

	public PhpConstantStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, @Nullable String valuePresentation)
	{
		super(parent, elementType, name, (short) 0);
		myValuePresentation = valuePresentation;
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
		return myValuePresentation;
	}
}
