package org.consulo.php.lang.psi.impl.stub;

import com.intellij.psi.stubs.PsiFileStubImpl;
import org.consulo.php.lang.psi.impl.PhpFileImpl;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStub extends PsiFileStubImpl<PhpFileImpl> {
	public PhpFileStub(PhpFileImpl file) {
		super(file);
	}
}
