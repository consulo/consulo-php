package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;

/**
 * @author VISTALL
 * @since 2019-04-10
 */
public class PhpParameterStubImpl extends PhpNamedStubImpl<Parameter> implements PhpParameterStub
{
	public PhpParameterStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name)
	{
		super(parent, elementType, name);
	}

	public PhpParameterStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name)
	{
		super(parent, elementType, name);
	}

	@Override
	public boolean isPassByRef()
	{
		return false;
	}

	@Override
	public boolean isOptional()
	{
		return false;
	}

	@Override
	public boolean isVariadic()
	{
		return false;
	}

	@Nullable
	@Override
	public String getDefaultValuePresentation()
	{
		return null;
	}
}
