package org.consulo.php.psi.impl.stub.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import net.jay.plugins.php.lang.psi.elements.PhpClass;
import net.jay.plugins.php.lang.psi.elements.impl.PhpClassImpl;
import org.consulo.php.index.PhpIndexKeys;
import org.consulo.php.psi.impl.stub.PhpClassStub;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpClassStubElement extends PhpStubElement<PhpClassStub, PhpClass> {
	public PhpClassStubElement() {
		super("PHP_CLASS");
	}

	@Override
	public PhpClass createPsi(ASTNode node) {
		return new PhpClassImpl(node);
	}

	@Override
	public PhpClass createPsi(@NotNull PhpClassStub phpClassStub) {
		return new PhpClassImpl(phpClassStub);
	}

	@Override
	public PhpClassStub createStub(@NotNull PhpClass phpClass, StubElement stubElement) {
		return new PhpClassStub(stubElement, phpClass.getName());
	}

	@Override
	public void serialize(@NotNull PhpClassStub phpClassStub, @NotNull StubOutputStream stubOutputStream) throws IOException {
		stubOutputStream.writeName(phpClassStub.getName());
	}

	@NotNull
	@Override
	public PhpClassStub deserialize(@NotNull StubInputStream stubInputStream, StubElement stubElement) throws IOException {
		StringRef name = stubInputStream.readName();
		return new PhpClassStub(stubElement, name.getString());
	}

	@Override
	public void indexStub(@NotNull PhpClassStub phpClassStub, @NotNull IndexSink indexSink) {
		String name = phpClassStub.getName();
		if(name != null) {
			indexSink.occurrence(PhpIndexKeys.CLASSES, name);
		}
	}
}
