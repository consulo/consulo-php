package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.language.Language;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.language.editor.structureView.TemplateLanguageStructureViewBuilder;
import consulo.language.psi.PsiFile;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
@ExtensionImpl
public class PhpStructureViewBuilderProvider implements PsiStructureViewFactory
{
	@Override
	@Nullable
	public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile)
	{
		return TemplateLanguageStructureViewBuilder.create(psiFile, (mainFile, editor) -> new PhpStructureViewModel((PhpFileImpl) mainFile));
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
