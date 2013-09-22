package org.consulo.php.lang.psi.impl.stub;

import org.consulo.php.lang.psi.impl.PhpFileImpl;
import com.intellij.psi.stubs.PsiFileStubImpl;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStub extends PsiFileStubImpl<PhpFileImpl>
{
	public PhpFileStub(PhpFileImpl file)
	{
		super(file);
	}
}
