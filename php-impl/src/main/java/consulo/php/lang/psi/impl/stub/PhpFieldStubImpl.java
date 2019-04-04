package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpFieldStubImpl extends PhpMemberStubImpl<Field> implements PhpFieldStub
{
	public PhpFieldStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name)
	{
		super(parent, elementType, name);
	}

	public PhpFieldStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name)
	{
		super(parent, elementType, name);
	}

	@Override
	public boolean isConstant()
	{
		return false;
	}

	@Override
	public String getDefaultValuePresentation()
	{
		return null;
	}
}
