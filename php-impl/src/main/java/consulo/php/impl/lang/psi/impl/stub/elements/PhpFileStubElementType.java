package consulo.php.impl.lang.psi.impl.stub.elements;

import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.PhpFile;
import consulo.language.psi.PsiFile;
import consulo.language.psi.stub.*;
import consulo.php.impl.lang.psi.impl.stub.PhpFileStubImpl;
import consulo.virtualFileSystem.VirtualFile;

import jakarta.annotation.Nonnull;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 16.07.13.
 */
public class PhpFileStubElementType extends IStubFileElementType<PhpFileStubImpl>
{
	public PhpFileStubElementType()
	{
		super("PHP_FILE", PhpLanguage.INSTANCE);
	}

	@Override
	public StubBuilder getBuilder()
	{
		return new DefaultStubBuilder()
		{
			@Nonnull
			@Override
			protected StubElement createStubForFile(@Nonnull PsiFile file)
			{
				if(file instanceof PhpFile)
				{
					return new PhpFileStubImpl((PhpFile) file);
				}

				return super.createStubForFile(file);
			}
		};
	}

	@Nonnull
	@Override
	public PhpFileStubImpl deserialize(@Nonnull final StubInputStream dataStream, final StubElement parentStub) throws IOException
	{
		return new PhpFileStubImpl(null);
	}

	@Override
	public int getStubVersion()
	{
		return super.getStubVersion() + 45;
	}

	@Nonnull
	@Override
	public String getExternalId()
	{
		return "php.file";
	}

	@Override
	public boolean shouldBuildStubFor(VirtualFile file)
	{
		return true;
	}
}
