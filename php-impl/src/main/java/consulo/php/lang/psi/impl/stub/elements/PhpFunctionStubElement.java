package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpFunction;
import consulo.php.lang.psi.impl.PhpFunctionImpl;
import consulo.php.lang.psi.impl.stub.PhpFunctionStub;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFunctionStubElement extends PhpStubElement<PhpFunctionStub, PhpFunction>
{
	public PhpFunctionStubElement()
	{
		super("PHP_FUNCTION");
	}

	@Nonnull
	@Override
	public PhpFunction createElement(ASTNode node)
	{
		return new PhpFunctionImpl(node);
	}

	@Override
	public PhpFunction createPsi(@Nonnull PhpFunctionStub stub)
	{
		return new PhpFunctionImpl(stub);
	}

	@Override
	public PhpFunctionStub createStub(@Nonnull PhpFunction psi, StubElement parentStub)
	{
		return new PhpFunctionStub(parentStub, StringRef.fromNullableString(psi.getName()));
	}

	@Override
	public void serialize(@Nonnull PhpFunctionStub stub, @Nonnull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getName());
	}

	@Nonnull
	@Override
	public PhpFunctionStub deserialize(@Nonnull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef name = dataStream.readName();

		return new PhpFunctionStub(parentStub, name);
	}

	@Override
	public void indexStub(@Nonnull PhpFunctionStub stub, @Nonnull IndexSink sink)
	{

	}
}
