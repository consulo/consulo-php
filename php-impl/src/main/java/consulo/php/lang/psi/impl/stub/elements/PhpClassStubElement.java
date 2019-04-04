package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
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
	public PhpClassStubImpl createStub(@Nonnull PhpClass phpClass, StubElement stubElement)
	{
		return new PhpClassStubImpl(stubElement, phpClass.getNamespaceName(), phpClass.getName());
	}

	@Override
	public void serialize(@Nonnull PhpClassStub phpClassStub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(phpClassStub.getNamespaceName());
		stubOutputStream.writeName(phpClassStub.getName());
	}

	@Nonnull
	@Override
	public PhpClassStubImpl deserialize(@Nonnull StubInputStream stubInputStream, StubElement stubElement) throws IOException
	{
		StringRef namespace = stubInputStream.readName();
		StringRef name = stubInputStream.readName();
		return new PhpClassStubImpl(stubElement, namespace, name);
	}

	@Override
	public void indexStub(@Nonnull PhpClassStub phpClassStub, @Nonnull IndexSink indexSink)
	{
		String namespace = phpClassStub.getNamespaceName();
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
