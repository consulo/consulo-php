package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.stubs.PhpFieldStub;
import consulo.annotations.RequiredReadAction;
import consulo.php.lang.psi.impl.PhpFieldImpl;
import consulo.php.lang.psi.impl.stub.PhpFieldStubImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 29.10.13.
 */
public class PhpFieldStubElement extends PhpStubElement<PhpFieldStub, Field>
{
	public PhpFieldStubElement()
	{
		super("PHP_FIELD");
	}

	@Nonnull
	@Override
	public Field createElement(ASTNode node)
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
		return new PhpFieldStubImpl(stubElement, this, phpField.getName());
	}

	@Override
	public void serialize(@Nonnull PhpFieldStub phpFieldStub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(phpFieldStub.getName());
	}

	@Nonnull
	@Override
	public PhpFieldStub deserialize(@Nonnull StubInputStream stubInputStream, StubElement stubElement) throws IOException
	{
		StringRef ref = stubInputStream.readName();
		return new PhpFieldStubImpl(stubElement, this, ref);
	}

	@Override
	public void indexStub(@Nonnull PhpFieldStub phpFieldStub, @Nonnull IndexSink indexSink)
	{

	}
}
