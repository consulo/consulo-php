package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nullable;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;

/**
 * @author VISTALL
 * @since 2019-04-09
 */
public class PhpNamespaceStubImpl extends PhpNamedStubImpl<PhpNamespace> implements PhpNamespaceStub
{
	public PhpNamespaceStubImpl(StubElement parent, IStubElementType elementType, @Nullable StringRef name)
	{
		super(parent, elementType, name);
	}

	public PhpNamespaceStubImpl(StubElement parent, IStubElementType elementType, @Nullable String name)
	{
		super(parent, elementType, name);
	}

	@Override
	public StringRef getParentNamespaceName()
	{
		return null;
	}
}
