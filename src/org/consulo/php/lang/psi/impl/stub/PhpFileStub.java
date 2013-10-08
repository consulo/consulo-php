package org.consulo.php.lang.psi.impl.stub;

import org.consulo.php.lang.psi.PhpFile;
import org.consulo.php.lang.psi.PhpStubElements;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStub extends PsiFileStubImpl<PhpFile>
{
	public PhpFileStub(PhpFile file)
	{
		super(file);
	}

	@Override
	public IStubFileElementType getType()
	{
		return PhpStubElements.FILE;
	}
}
