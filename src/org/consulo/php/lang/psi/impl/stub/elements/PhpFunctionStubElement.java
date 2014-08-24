package org.consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.impl.PhpFunctionImpl;
import org.consulo.php.lang.psi.impl.stub.PhpFunctionStub;
import org.jetbrains.annotations.NotNull;
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

	@NotNull
	@Override
	public PhpFunction createElement(ASTNode node)
	{
		return new PhpFunctionImpl(node);
	}

	@Override
	public PhpFunction createPsi(@NotNull PhpFunctionStub stub)
	{
		return new PhpFunctionImpl(stub);
	}

	@Override
	public PhpFunctionStub createStub(@NotNull PhpFunction psi, StubElement parentStub)
	{
		return new PhpFunctionStub(parentStub, StringRef.fromNullableString(psi.getName()));
	}

	@Override
	public void serialize(@NotNull PhpFunctionStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getName());
	}

	@NotNull
	@Override
	public PhpFunctionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef name = dataStream.readName();

		return new PhpFunctionStub(parentStub, name);
	}

	@Override
	public void indexStub(@NotNull PhpFunctionStub stub, @NotNull IndexSink sink)
	{

	}
}
