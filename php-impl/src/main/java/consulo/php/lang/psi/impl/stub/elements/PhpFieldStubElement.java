package consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.impl.PhpFieldImpl;
import consulo.php.lang.psi.impl.stub.PhpFieldStub;
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
public class PhpFieldStubElement extends PhpStubElement<PhpFieldStub, PhpField>
{
	public PhpFieldStubElement()
	{
		super("PHP_FIELD");
	}

	@Nonnull
	@Override
	public PhpField createElement(ASTNode node)
	{
		return new PhpFieldImpl(node);
	}

	@Override
	public PhpField createPsi(@Nonnull PhpFieldStub phpFieldStub)
	{
		return new PhpFieldImpl(phpFieldStub);
	}

	@Override
	public PhpFieldStub createStub(@Nonnull PhpField phpField, StubElement stubElement)
	{
		return new PhpFieldStub(stubElement, StringRef.fromNullableString(phpField.getName()));
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
		return new PhpFieldStub(stubElement, ref);
	}

	@Override
	public void indexStub(@Nonnull PhpFieldStub phpFieldStub, @Nonnull IndexSink indexSink)
	{

	}
}
