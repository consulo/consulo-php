package net.jay.plugins.php.lang;

import gnu.trove.THashSet;

import java.util.Arrays;
import java.util.Set;

import net.jay.plugins.php.lang.lexer.PHPTokenTypes;
import net.jay.plugins.php.lang.parser.PHPElementTypes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
public class PHPFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements TemplateLanguageFileViewProvider
{
	private static final Set<Language> ourRelevantLanguages = new THashSet<Language>(Arrays.asList(StdLanguages.HTML, PHPLanguage.INSTANCE));

	public PHPFileViewProvider(PsiManager manager, VirtualFile file, boolean physical)
	{
		super(manager, file, physical);
	}

	@NotNull
	public Set<Language> getLanguages()
	{
		return ourRelevantLanguages;
	}

	@Override
	@NotNull
	public Language getBaseLanguage()
	{
		return PHPLanguage.INSTANCE;
	}

	@NotNull
	public Language getTemplateDataLanguage()
	{
		return StdLanguages.HTML;
	}

	private static TemplateDataElementType ourTemplateDataType = new TemplateDataElementType("TEMPLATE_DATA in PHP", PHPLanguage.INSTANCE, PHPTokenTypes.HTML, PHPElementTypes.PHP_OUTER_TYPE);

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
		return new PHPFileViewProvider(getManager(), virtualFile, false);
	}
}