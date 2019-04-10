package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.jetbrains.php.lang.psi.elements.Method;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.impl.PhpClassMethodImpl;
import consulo.php.lang.psi.impl.stub.PhpClassMethodStubImpl;

/**
 * @author VISTALL
 * @since 30.03.2019
 */
public class PhpClassMethodStubElementType extends PhpStubElementType<PhpClassMethodStubImpl, Method>
{
	public PhpClassMethodStubElementType()
	{
		super("PHP_CLASS_METHOD");
	}

	@Nonnull
	@Override
	public Method createElement(@Nonnull ASTNode node)
	{
		return new PhpClassMethodImpl(node);
	}

	@Override
	public Method createPsi(@Nonnull PhpClassMethodStubImpl stub)
	{
		return new PhpClassMethodImpl(stub);
	}

	@RequiredReadAction
	@Override
	public PhpClassMethodStubImpl createStub(@Nonnull Method psi, StubElement parentStub)
	{
		return new PhpClassMethodStubImpl(parentStub, this, psi.getName(), (short) 0);
	}

	@Override
	public void serialize(@Nonnull PhpClassMethodStubImpl stub, @Nonnull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getName());
	}

	@Nonnull
	@Override
	public PhpClassMethodStubImpl deserialize(@Nonnull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef name = dataStream.readName();

		return new PhpClassMethodStubImpl(parentStub, this, name, (short) 0);
	}

	@Override
	public void indexStub(@Nonnull PhpClassMethodStubImpl stub, @Nonnull IndexSink sink)
	{

	}
}
