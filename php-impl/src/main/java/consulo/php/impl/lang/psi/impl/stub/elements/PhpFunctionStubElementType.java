package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.stubs.PhpFunctionStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.index.io.StringRef;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import consulo.php.impl.index.PhpIndexKeys;
import consulo.php.impl.lang.psi.impl.PhpFunctionImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpFunctionStubImpl;
import consulo.util.lang.StringUtil;

import jakarta.annotation.Nonnull;
import java.io.IOException;

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
		return new PhpFunctionStubImpl(parentStub, psi.getName(), (short) 0);
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

		return new PhpFunctionStubImpl(parentStub, name, (short) 0);
	}

	@Override
	public void indexStub(@Nonnull PhpFunctionStub stub, @Nonnull IndexSink sink)
	{
		String name = stub.getName();
		if(!StringUtil.isEmpty(name))
		{
			sink.occurrence(PhpIndexKeys.FUNCTION_BY_NAME, name);
		}
	}
}
