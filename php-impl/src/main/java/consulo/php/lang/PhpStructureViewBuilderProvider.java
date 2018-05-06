package consulo.php.lang;

import consulo.php.PhpBundle;
import consulo.php.lang.psi.impl.PhpFileImpl;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.ide.structureView.StructureView;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.ide.structureView.impl.StructureViewComposite;
import com.intellij.ide.structureView.impl.TemplateLanguageStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.psi.PsiFile;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
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

}
