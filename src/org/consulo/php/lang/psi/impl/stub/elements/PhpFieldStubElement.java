package org.consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import org.consulo.php.lang.psi.PhpField;
import org.consulo.php.lang.psi.impl.PhpFieldImpl;
import org.consulo.php.lang.psi.impl.stub.PhpFieldStub;
import org.jetbrains.annotations.NotNull;
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

	@Override
	public PhpField createPsi(ASTNode node)
	{
		return new PhpFieldImpl(node);
	}

	@Override
	public PhpField createPsi(@NotNull PhpFieldStub phpFieldStub)
	{
		return new PhpFieldImpl(phpFieldStub);
	}

	@Override
	public PhpFieldStub createStub(@NotNull PhpField phpField, StubElement stubElement)
	{
		return new PhpFieldStub(stubElement, StringRef.fromNullableString(phpField.getName()));
	}

	@Override
	public void serialize(@NotNull PhpFieldStub phpFieldStub, @NotNull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(phpFieldStub.getName());
	}

	@NotNull
	@Override
	public PhpFieldStub deserialize(@NotNull StubInputStream stubInputStream, StubElement stubElement) throws IOException
	{
		StringRef ref = stubInputStream.readName();
		return new PhpFieldStub(stubElement, ref);
	}

	@Override
	public void indexStub(@NotNull PhpFieldStub phpFieldStub, @NotNull IndexSink indexSink)
	{

	}
}
