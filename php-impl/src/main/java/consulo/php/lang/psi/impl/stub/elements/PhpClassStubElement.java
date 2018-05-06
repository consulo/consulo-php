package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import consulo.php.index.PhpIndexKeys;
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.impl.PhpClassImpl;
import consulo.php.lang.psi.impl.stub.PhpClassStub;
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
public class PhpClassStubElement extends PhpStubElement<PhpClassStub, PhpClass>
{
	public PhpClassStubElement()
	{
		super("PHP_CLASS");
	}

	@Nonnull
	@Override
	public PhpClass createElement(ASTNode node)
	{
		return new PhpClassImpl(node);
	}

	@Override
	public PhpClass createPsi(@Nonnull PhpClassStub phpClassStub)
	{
		return new PhpClassImpl(phpClassStub);
	}

	@Override
	public PhpClassStub createStub(@Nonnull PhpClass phpClass, StubElement stubElement)
	{
		return new PhpClassStub(stubElement, phpClass.getNamespace(), phpClass.getName());
	}

	@Override
	public void serialize(@Nonnull PhpClassStub phpClassStub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(phpClassStub.getNamespace());
		stubOutputStream.writeName(phpClassStub.getName());
	}

	@Nonnull
	@Override
	public PhpClassStub deserialize(@Nonnull StubInputStream stubInputStream, StubElement stubElement) throws IOException
	{
		StringRef namespace = stubInputStream.readName();
		StringRef name = stubInputStream.readName();
		return new PhpClassStub(stubElement, namespace, name);
	}

	@Override
	public void indexStub(@Nonnull PhpClassStub phpClassStub, @Nonnull IndexSink indexSink)
	{
		String namespace = phpClassStub.getNamespace();
		String name = phpClassStub.getName();
		if(name != null)
		{
			indexSink.occurrence(PhpIndexKeys.CLASSES, name);

			if(namespace.isEmpty())
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
