package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.index.io.StringRef;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import consulo.php.impl.index.PhpIndexKeys;
import consulo.php.impl.lang.psi.PhpClassSupertypes;
import consulo.util.collection.ArrayUtil;
import consulo.php.impl.lang.psi.impl.PhpClassImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpClassStubImpl;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.util.Locale;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassStubElementType extends PhpStubElementType<PhpClassStub, PhpClass>
{
	public PhpClassStubElementType()
	{
		super("PHP_CLASS");
	}

	@Nonnull
	@Override
	public PhpClass createElement(@Nonnull ASTNode node)
	{
		return new PhpClassImpl(node);
	}

	@Override
	public PhpClass createPsi(@Nonnull PhpClassStub phpClassStub)
	{
		return new PhpClassImpl(phpClassStub);
	}

	@RequiredReadAction
	@Override
	public PhpClassStubImpl createStub(@Nonnull PhpClass phpClass, StubElement stubElement)
	{
		short flags = PhpClassStubImpl.packFlags(phpClass);
		return new PhpClassStubImpl(stubElement, phpClass.getNamespaceName(), phpClass.getName(), flags,
				PhpClassSupertypes.superClassFqn(phpClass),
				ArrayUtil.toStringArray(PhpClassSupertypes.interfaceFqns(phpClass)),
				ArrayUtil.toStringArray(PhpClassSupertypes.traitFqns(phpClass)));
	}

	@Override
	public void serialize(@Nonnull PhpClassStub phpClassStub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(phpClassStub.getNamespaceName());
		stubOutputStream.writeName(phpClassStub.getName());
		stubOutputStream.writeShort(phpClassStub.getFlags());
		stubOutputStream.writeName(phpClassStub.getSuperclass());
		writeNames(stubOutputStream, phpClassStub.getInterfaces());
		writeNames(stubOutputStream, phpClassStub.getTraits());
	}

	@Nonnull
	@Override
	public PhpClassStubImpl deserialize(@Nonnull StubInputStream in, StubElement stubElement) throws IOException
	{
		StringRef namespace = in.readName();
		StringRef name = in.readName();
		short flags = in.readShort();
		StringRef superclass = in.readName();
		String[] interfaces = readNames(in);
		String[] traits = readNames(in);
		return new PhpClassStubImpl(stubElement, StringRef.toString(namespace), StringRef.toString(name), flags, StringRef.toString(superclass), interfaces, traits);
	}

	private static void writeNames(StubOutputStream out, String[] names) throws IOException
	{
		out.writeVarInt(names.length);
		for(String name : names)
		{
			out.writeName(name);
		}
	}

	private static String[] readNames(StubInputStream in) throws IOException
	{
		String[] names = new String[in.readVarInt()];
		for(int i = 0; i < names.length; i++)
		{
			names[i] = StringRef.toString(in.readName());
		}
		return names;
	}

	@Override
	public void indexStub(@Nonnull PhpClassStub phpClassStub, @Nonnull IndexSink indexSink)
	{
		String namespace = phpClassStub.getNamespaceName();
		String name = phpClassStub.getName();
		if(name != null)
		{
			// class names are case-insensitive in PHP
			indexSink.occurrence(PhpIndexKeys.CLASSES, name.toLowerCase(Locale.ROOT));

			String fqn = PhpLangUtil.toFQN(StringUtil.notNullize(namespace), name);
			indexSink.occurrence(PhpIndexKeys.FULL_FQ_CLASSES, fqn.toLowerCase(Locale.ROOT));
		}

		String superclass = phpClassStub.getSuperclass();
		if(superclass != null)
		{
			indexSink.occurrence(PhpIndexKeys.SUPERTYPES, superclass.toLowerCase(Locale.ROOT));
		}
		for(String anInterface : phpClassStub.getInterfaces())
		{
			indexSink.occurrence(PhpIndexKeys.SUPERTYPES, anInterface.toLowerCase(Locale.ROOT));
		}
	}
}
