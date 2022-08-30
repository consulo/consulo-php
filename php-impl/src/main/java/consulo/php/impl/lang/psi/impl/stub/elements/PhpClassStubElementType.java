package consulo.php.impl.lang.psi.impl.stub.elements;

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
import consulo.php.impl.lang.psi.impl.PhpClassImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpClassStubImpl;
import consulo.util.lang.StringUtil;

import javax.annotation.Nonnull;
import java.io.IOException;

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
		return new PhpClassStubImpl(stubElement, phpClass.getNamespaceName(), phpClass.getName(), flags);
	}

	@Override
	public void serialize(@Nonnull PhpClassStub phpClassStub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(phpClassStub.getNamespaceName());
		stubOutputStream.writeName(phpClassStub.getName());
		stubOutputStream.writeShort(phpClassStub.getFlags());
	}

	@Nonnull
	@Override
	public PhpClassStubImpl deserialize(@Nonnull StubInputStream in, StubElement stubElement) throws IOException
	{
		StringRef namespace = in.readName();
		StringRef name = in.readName();
		short flags = in.readShort();
		return new PhpClassStubImpl(stubElement, namespace, name, flags);
	}

	@Override
	public void indexStub(@Nonnull PhpClassStub phpClassStub, @Nonnull IndexSink indexSink)
	{
		String namespace = phpClassStub.getNamespaceName();
		String name = phpClassStub.getName();
		if(name != null)
		{
			indexSink.occurrence(PhpIndexKeys.CLASSES, name);

			if(StringUtil.isEmpty(namespace))
			{
				indexSink.occurrence(PhpIndexKeys.FULL_FQ_CLASSES, "\\" + name);
			}
			else
			{
				indexSink.occurrence(PhpIndexKeys.FULL_FQ_CLASSES, "\\" + namespace + "\\" + name);
			}
		}
	}
}
