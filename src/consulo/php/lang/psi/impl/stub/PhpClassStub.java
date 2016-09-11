package consulo.php.lang.psi.impl.stub;

import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpStubElements;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassStub extends NamedStubBase<PhpClass>
{
	private final StringRef myNamespace;

	public PhpClassStub(StubElement parent, String namespace, String name)
	{
		super(parent, PhpStubElements.CLASS, name);

		myNamespace = StringRef.fromNullableString(namespace);
	}

	public PhpClassStub(StubElement parent, StringRef namespace, StringRef name)
	{
		super(parent, PhpStubElements.CLASS, name);

		myNamespace = namespace;
	}

	public String getNamespace()
	{
		return StringRef.toString(myNamespace);
	}
}
