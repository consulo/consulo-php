package consulo.php.impl.lang.psi.impl.stub;

import com.jetbrains.php.lang.psi.PhpFile;
import consulo.language.psi.stub.IStubFileElementType;
import consulo.language.psi.stub.PsiFileStubImpl;
import consulo.php.impl.lang.psi.PhpStubElements;

import jakarta.annotation.Nonnull;

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
