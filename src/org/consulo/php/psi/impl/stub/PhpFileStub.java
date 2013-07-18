package org.consulo.php.psi.impl.stub;

import com.intellij.psi.stubs.PsiFileStubImpl;
import net.jay.plugins.php.lang.psi.PHPFile;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStub extends PsiFileStubImpl<PHPFile> {
	public PhpFileStub(PHPFile file) {
		super(file);
	}
}
