package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import consulo.index.io.StringRef;
import consulo.language.psi.stub.StubElement;
import consulo.php.impl.lang.psi.PhpStubElements;
import consulo.util.lang.BitUtil;

import jakarta.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassStubImpl extends PhpNamedStubImpl<PhpClass> implements PhpClassStub
{
	public static short packFlags(PhpClass phpClass)
	{
		int flags = 0;
		flags = BitUtil.set(flags, INTERFACE, phpClass.isInterface());
		flags = BitUtil.set(flags, TRAIT, phpClass.isTrait());
		flags = BitUtil.set(flags, ABSTRACT, phpClass.isAbstract());
		flags = BitUtil.set(flags, FINAL, phpClass.isFinal());
		return (short) flags;
	}

	public static final int INTERFACE = 1 << 0;
	public static final int TRAIT = 1 << 1;
	public static final int ABSTRACT = 1 << 2;
	public static final int FINAL = 1 << 3;

	private final String myNamespaceName;

	public PhpClassStubImpl(StubElement parent, StringRef namespace, StringRef name, short flags)
	{
		this(parent, StringRef.toString(namespace), StringRef.toString(name), flags);
	}

	public PhpClassStubImpl(StubElement parent, String namespace, String name, short flags)
	{
		super(parent, PhpStubElements.CLASS, name, flags);

		myNamespaceName = namespace;
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
		return BitUtil.isSet(myFlags, INTERFACE);
	}

	@Override
	public boolean isTrait()
	{
		return BitUtil.isSet(myFlags, TRAIT);
	}

	@Override
	public boolean isAbstract()
	{
		return BitUtil.isSet(myFlags, ABSTRACT);
	}

	@Override
	public boolean isFinal()
	{
		return BitUtil.isSet(myFlags, FINAL);
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
	public String getNamespaceName()
	{
		return myNamespaceName;
	}

	@Override
	public boolean isAlias()
	{
		return false;
	}

	@Nonnull
	@Override
	public PhpType getType()
	{
		return null;
	}
}
