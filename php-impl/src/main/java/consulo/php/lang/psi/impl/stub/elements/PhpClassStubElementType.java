package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.index.PhpIndexKeys;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.php.lang.psi.impl.PhpClassImpl;
import consulo.php.lang.psi.impl.stub.PhpClassStubImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;

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
				indexSink.occurrence(PhpIndexKeys.FULL_FQ_CLASSES, name);
			}
			else
			{
				indexSink.occurrence(PhpIndexKeys.FULL_FQ_CLASSES, namespace + "\\" + name);
			}
		}
	}
}
