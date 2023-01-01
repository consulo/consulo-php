package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.index.io.StringRef;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import consulo.php.impl.lang.psi.impl.PhpParameterImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpParameterStubImpl;
import consulo.util.lang.BitUtil;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 2019-04-10
 */
public class PhpParameterStubElementType extends PhpStubElementType<PhpParameterStub, Parameter>
{
	public PhpParameterStubElementType()
	{
		super("PHP_PARAMETER");
	}

	@Nonnull
	@Override
	public Parameter createElement(@Nonnull ASTNode node)
	{
		return new PhpParameterImpl(node);
	}

	@Override
	public Parameter createPsi(@Nonnull PhpParameterStub stub)
	{
		return new PhpParameterImpl(stub, this);
	}

	@RequiredReadAction
	@Override
	public PhpParameterStub createStub(@Nonnull Parameter psi, StubElement parentStub)
	{
		int flags = 0;
		flags = BitUtil.set(flags, PhpParameterStubImpl.VARIADIC, psi.isVariadic());
		return new PhpParameterStubImpl(parentStub, this, psi.getName(), (short) flags);
	}

	@Override
	public void serialize(@Nonnull PhpParameterStub stub, @Nonnull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getName());
		dataStream.writeShort(stub.getFlags());
	}

	@Nonnull
	@Override
	public PhpParameterStub deserialize(@Nonnull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef name = dataStream.readName();
		short flags = dataStream.readShort();

		return new PhpParameterStubImpl(parentStub, this, name, flags);
	}

	@Override
	public void indexStub(@Nonnull PhpParameterStub stub, @Nonnull IndexSink sink)
	{

	}
}