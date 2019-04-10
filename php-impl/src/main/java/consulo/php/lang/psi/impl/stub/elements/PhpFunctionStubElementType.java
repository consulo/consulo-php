package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.stubs.PhpFunctionStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.impl.PhpFunctionImpl;
import consulo.php.lang.psi.impl.stub.PhpFunctionStubImpl;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpFunctionStubElementType extends PhpStubElementType<PhpFunctionStub, Function>
{
	public PhpFunctionStubElementType()
	{
		super("PHP_FUNCTION");
	}

	@Nonnull
	@Override
	public Function createElement(@Nonnull ASTNode node)
	{
		return new PhpFunctionImpl(node);
	}

	@Override
	public Function createPsi(@Nonnull PhpFunctionStub stub)
	{
		return new PhpFunctionImpl(stub);
	}

	@RequiredReadAction
	@Override
	public PhpFunctionStubImpl createStub(@Nonnull Function psi, StubElement parentStub)
	{
		return new PhpFunctionStubImpl(parentStub, psi.getName());
	}

	@Override
	public void serialize(@Nonnull PhpFunctionStub stub, @Nonnull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getName());
	}

	@Nonnull
	@Override
	public PhpFunctionStubImpl deserialize(@Nonnull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef name = dataStream.readName();

		return new PhpFunctionStubImpl(parentStub, name);
	}

	@Override
	public void indexStub(@Nonnull PhpFunctionStub stub, @Nonnull IndexSink sink)
	{

	}
}
