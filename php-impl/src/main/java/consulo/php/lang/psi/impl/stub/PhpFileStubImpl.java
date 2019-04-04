package consulo.php.lang.psi.impl.stub;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.PhpFile;
import consulo.php.lang.psi.PhpStubElements;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStubImpl extends PsiFileStubImpl<PhpFile>
{
	public PhpFileStubImpl(PhpFile file)
	{
		super(file);
	}

	@Nonnull
	@Override
	public IStubFileElementType getType()
	{
		return PhpStubElements.FILE;
	}
}
