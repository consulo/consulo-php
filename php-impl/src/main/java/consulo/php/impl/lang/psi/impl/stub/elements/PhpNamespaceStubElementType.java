package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.index.io.StringRef;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import consulo.php.impl.index.PhpIndexKeys;
import consulo.php.impl.lang.psi.impl.PhpNamespaceStatementImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpNamespaceStubImpl;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 2019-04-09
 */
public class PhpNamespaceStubElementType extends PhpStubElementType<PhpNamespaceStub, PhpNamespace>
{
	public PhpNamespaceStubElementType()
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
		return new PhpNamespaceStubImpl(stubElement, this, phpNamespace.getName(), (short) 0);
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
		return new PhpNamespaceStubImpl(stubElement, this, ref, (short) 0);
	}

	@Override
	public void indexStub(@Nonnull PhpNamespaceStub stub, @Nonnull IndexSink indexSink)
	{
		String name = stub.getName();
		if(!StringUtil.isEmpty(name))
		{
			indexSink.occurrence(PhpIndexKeys.NAMESPACES, "\\" + name);
		}
	}
}