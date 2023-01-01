package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.file.FileViewProvider;
import consulo.language.file.LanguageFileViewProviderFactory;
import consulo.language.psi.PsiManager;
import consulo.virtualFileSystem.VirtualFile;

import javax.annotation.Nonnull;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
@ExtensionImpl
public class PhpFileViewProviderFactory implements LanguageFileViewProviderFactory
{
	@Override
	public FileViewProvider createFileViewProvider(final VirtualFile file, final Language language, final PsiManager manager, final boolean physical)
	{
		return new PhpFileViewProvider(manager, file, physical);
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}