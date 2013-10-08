package org.consulo.php.lang.psi.impl.stub.elements;

import java.io.IOException;

import org.consulo.php.lang.psi.PhpGroup;
import org.consulo.php.lang.psi.impl.PhpGroupImpl;
import org.consulo.php.lang.psi.impl.stub.PhpGroupStub;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;

/**
 * @author VISTALL
 * @since 22.09.13.
 */
public class PhpGroupStubElement extends PhpStubElement<PhpGroupStub, PhpGroup>
{
	public PhpGroupStubElement()
	{
		super("PHP_GROUP");
	}

	@Override
	public PhpGroup createPsi(ASTNode node)
	{
		return new PhpGroupImpl(node);
	}

	@Override
	public PhpGroup createPsi(@NotNull PhpGroupStub stub)
	{
		return new PhpGroupImpl(stub);
	}

	@Override
	public PhpGroupStub createStub(@NotNull PhpGroup psi, StubElement parentStub)
	{
		return new PhpGroupStub(parentStub);
	}

	@Override
	public void serialize(@NotNull PhpGroupStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{

	}

	@NotNull
	@Override
	public PhpGroupStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PhpGroupStub(parentStub);
	}

	@Override
	public void indexStub(@NotNull PhpGroupStub stub, @NotNull IndexSink sink)
	{

	}
}
