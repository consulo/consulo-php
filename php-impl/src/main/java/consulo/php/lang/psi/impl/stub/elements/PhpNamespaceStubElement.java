package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.impl.PhpNamespaceStatementImpl;
import consulo.php.lang.psi.impl.stub.PhpNamespaceStubImpl;

/**
 * @author VISTALL
 * @since 2019-04-09
 */
public class PhpNamespaceStubElement extends PhpStubElement<PhpNamespaceStub, PhpNamespace>
{
	public PhpNamespaceStubElement()
	{
		super("PHP_NAMESPACE");
	}

	@Nonnull
	@Override
	public PhpNamespace createElement(@Nonnull ASTNode node)
	{
		return new PhpNamespaceStatementImpl(node);
	}

	@Override
	public PhpNamespace createPsi(@Nonnull PhpNamespaceStub stub)
	{
		return new PhpNamespaceStatementImpl(stub, this);
	}

	@RequiredReadAction
	@Override
	public PhpNamespaceStub createStub(@Nonnull PhpNamespace phpNamespace, StubElement stubElement)
	{
		return new PhpNamespaceStubImpl(stubElement, this, phpNamespace.getName());
	}

	@Override
	public void serialize(@Nonnull PhpNamespaceStub stub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(stub.getName());
	}

	@Nonnull
	@Override
	public PhpNamespaceStub deserialize(@Nonnull StubInputStream stubInputStream, StubElement stubElement) throws IOException
	{
		StringRef ref = stubInputStream.readName();
		return new PhpNamespaceStubImpl(stubElement, this, ref);
	}

	@Override
	public void indexStub(@Nonnull PhpNamespaceStub phpFieldStub, @Nonnull IndexSink indexSink)
	{

	}
}