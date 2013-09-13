package org.consulo.php.psi.impl.stub;

import com.intellij.psi.stubs.PsiFileStubImpl;
import org.consulo.php.lang.psi.PhpFile;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStub extends PsiFileStubImpl<PhpFile> {
	public PhpFileStub(PhpFile file) {
		super(file);
	}
}
