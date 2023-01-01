package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.language.Language;
import consulo.language.impl.file.MultiplePsiFilesPerDocumentFileViewProvider;
import consulo.language.impl.psi.PsiFileImpl;
import consulo.language.impl.psi.template.TemplateDataElementType;
import consulo.language.parser.ParserDefinition;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.language.template.TemplateLanguageFileViewProvider;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.virtualFileSystem.VirtualFile;
import consulo.xml.lang.html.HTMLLanguage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
public class PhpFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements TemplateLanguageFileViewProvider
{
	private static final Set<Language> ourRelevantLanguages = new HashSet<Language>(Arrays.asList(HTMLLanguage.INSTANCE, PhpLanguage.INSTANCE));

	public PhpFileViewProvider(PsiManager manager, VirtualFile file, boolean physical)
	{
		super(manager, file, physical);
	}

	@Override
	@Nonnull
	public Set<Language> getLanguages()
	{
		return ourRelevantLanguages;
	}

	@Override
	@Nonnull
	public Language getBaseLanguage()
	{
		return PhpLanguage.INSTANCE;
	}

	@Override
	@Nonnull
	public Language getTemplateDataLanguage()
	{
		return HTMLLanguage.INSTANCE;
	}

	private static TemplateDataElementType ourTemplateDataType = new TemplateDataElementType("TEMPLATE_DATA in PHP", PhpLanguage.INSTANCE, PhpTokenTypes.HTML, PhpElementTypes.PHP_OUTER_TYPE);

	@Override
	@Nullable
	protected PsiFile createFile(final Language lang)
	{
		if(lang == getTemplateDataLanguage())
		{
			final PsiFileImpl file = (PsiFileImpl) ParserDefinition.forLanguage(lang).createFile(this);
			file.setContentElementType(ourTemplateDataType);
			return file;
		}

		if(lang == getBaseLanguage())
		{
			return ParserDefinition.forLanguage(lang).createFile(this);
		}
		return null;
	}

	@Override
	protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile virtualFile)
	{
		return new PhpFileViewProvider(getManager(), virtualFile, false);
	}                                                                       
}
