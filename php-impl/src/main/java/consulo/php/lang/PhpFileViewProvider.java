package consulo.php.lang;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.jetbrains.php.lang.PhpLanguage;
import consulo.php.lang.lexer.PhpTokenTypes;
import consulo.php.lang.parser.PhpElementTypes;

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
			final PsiFileImpl file = (PsiFileImpl) LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
			file.setContentElementType(ourTemplateDataType);
			return file;
		}

		if(lang == getBaseLanguage())
		{
			return LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
		}
		return null;
	}

	@Override
	protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile virtualFile)
	{
		return new PhpFileViewProvider(getManager(), virtualFile, false);
	}
}
