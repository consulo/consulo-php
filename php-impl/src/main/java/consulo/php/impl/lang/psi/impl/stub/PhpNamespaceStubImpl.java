package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.StubElement;

import javax.annotation.Nullable;

/**
 * @author VISTALL
 * @since 2019-04-09
 */
public class PhpNamespaceStubImpl extends PhpNamedStubImpl<PhpNamespace> implements PhpNamespaceStub
{
	public PhpNamespaceStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	public PhpNamespaceStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name, short flags)
	{
		super(parent, elementType, name, flags);
	}

	@Override
	public StringRef getParentNamespaceName()
	{
		return null;
	}
}
