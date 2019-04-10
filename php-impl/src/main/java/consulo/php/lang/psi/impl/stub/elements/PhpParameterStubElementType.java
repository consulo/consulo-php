package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.impl.PhpParameterImpl;
import consulo.php.lang.psi.impl.stub.PhpParameterStubImpl;

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
		return new PhpParameterStubImpl(parentStub, this, psi.getName());
	}

	@Override
	public void serialize(@Nonnull PhpParameterStub stub, @Nonnull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getName());
	}

	@Nonnull
	@Override
	public PhpParameterStub deserialize(@Nonnull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef name = dataStream.readName();

		return new PhpParameterStubImpl(parentStub, this, name);
	}

	@Override
	public void indexStub(@Nonnull PhpParameterStub stub, @Nonnull IndexSink sink)
	{

	}
}