package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.index.io.StringRef;
import consulo.language.ast.ASTNode;
import consulo.language.psi.stub.IndexSink;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import consulo.php.impl.lang.psi.impl.PhpFieldImpl;
import consulo.php.impl.lang.psi.impl.stub.PhpFieldStubImpl;

import jakarta.annotation.Nonnull;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpFieldStubElementType extends PhpStubElementType<PhpFieldStub, Field>
{
	public PhpFieldStubElementType()
	{
		super("PHP_FIELD");
	}

	@Nonnull
	@Override
	public Field createElement(@Nonnull ASTNode node)
	{
		return new PhpFieldImpl(node);
	}

	@Override
	public Field createPsi(@Nonnull PhpFieldStub stub)
	{
		return new PhpFieldImpl(stub);
	}

	@RequiredReadAction
	@Override
	public PhpFieldStubImpl createStub(@Nonnull Field phpField, StubElement stubElement)
	{
		short flags = PhpFieldStubImpl.packFlags(phpField);
		return new PhpFieldStubImpl(stubElement, this, phpField.getName(), flags);
	}

	@Override
	public void serialize(@Nonnull PhpFieldStub stub, @Nonnull StubOutputStream stream) throws IOException
	{
		stream.writeName(stub.getName());
		stream.writeShort(stub.getFlags());
	}

	@Nonnull
	@Override
	public PhpFieldStub deserialize(@Nonnull StubInputStream stream, StubElement stubElement) throws IOException
	{
		StringRef ref = stream.readName();
		short flags = stream.readShort();
		return new PhpFieldStubImpl(stubElement, this, ref, flags);
	}

	@Override
	public void indexStub(@Nonnull PhpFieldStub phpFieldStub, @Nonnull IndexSink indexSink)
	{

	}
}
