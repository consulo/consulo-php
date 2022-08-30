package consulo.php.impl.lang;

import com.jetbrains.php.lang.PhpLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.fileEditor.FileEditor;
import consulo.fileEditor.structureView.StructureView;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.ide.impl.idea.ide.structureView.impl.StructureViewComposite;
import consulo.ide.impl.idea.ide.structureView.impl.TemplateLanguageStructureViewBuilder;
import consulo.language.Language;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.language.psi.PsiFile;
import consulo.php.PhpBundle;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
		return new TemplateLanguageStructureViewBuilder(psiFile)
		{
			@Override
			protected StructureViewComposite.StructureViewDescriptor createMainView(final FileEditor fileEditor, final PsiFile mainFile)
			{
				StructureView mainView = new TreeBasedStructureViewBuilder()
				{
					@Override
					@Nonnull
					public StructureViewModel createStructureViewModel(Editor editor)
					{
						return new PhpStructureViewModel((PhpFileImpl) mainFile);
					}
				}.createStructureView(fileEditor, mainFile.getProject());
				return new StructureViewComposite.StructureViewDescriptor(PhpBundle.message("tab.structureview.view"), mainView, mainFile.getFileType().getIcon());
			}
		};
	}

	@Nonnull
	@Override
	public Language getLanguage()
	{
		return PhpLanguage.INSTANCE;
	}
}
