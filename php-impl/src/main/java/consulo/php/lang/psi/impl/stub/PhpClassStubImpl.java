package consulo.php.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import consulo.php.lang.psi.PhpStubElements;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassStubImpl extends NamedStubBase<PhpClass> implements PhpClassStub
{
	private final String myNamespaceName;

	public PhpClassStubImpl(StubElement parent, String namespace, String name)
	{
		super(parent, PhpStubElements.CLASS, name);

		myNamespaceName = namespace;
	}

	public PhpClassStubImpl(StubElement parent, StringRef namespace, StringRef name)
	{
		super(parent, PhpStubElements.CLASS, name);

		myNamespaceName = StringRef.toString(namespace);
	}

	@Override
	public String getSuperclass()
	{
		return null;
	}

	@Override
	public String[] getInterfaces()
	{
		return new String[0];
	}

	@Override
	public String[] getTraits()
	{
		return new String[0];
	}

	@Override
	public String[] getMixins()
	{
		return new String[0];
	}

	@Override
	public boolean isInterface()
	{
		return false;
	}

	@Override
	public boolean isTrait()
	{
		return false;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public boolean isFinal()
	{
		return false;
	}

	@Override
	public boolean hasOwnStaticMembers()
	{
		return false;
	}

	@Override
	public boolean hasMethodTags()
	{
		return false;
	}

	@Override
	public boolean hasPropertyTags()
	{
		return false;
	}

	@Override
	public boolean hasConstructorFields()
	{
		return false;
	}

	@Override
	public boolean hasTraitUses()
	{
		return false;
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
	public String getNamespaceName()
	{
		return myNamespaceName;
	}

	@Override
	public boolean isAlias()
	{
		return false;
	}

	@Override
	public PhpType getType()
	{
		return null;
	}
}
