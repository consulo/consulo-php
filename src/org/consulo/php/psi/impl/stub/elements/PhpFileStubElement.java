package org.consulo.php.psi.impl.stub.elements;

import com.intellij.psi.tree.IStubFileElementType;
import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.psi.impl.stub.PhpFileStub;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStubElement extends IStubFileElementType<PhpFileStub> {
	public PhpFileStubElement() {
		super("PHP_FILE", PhpLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getExternalId() {
		return "php.file";
	}
}
