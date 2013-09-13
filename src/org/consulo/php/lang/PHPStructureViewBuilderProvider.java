package org.consulo.php.lang;

import org.consulo.php.PHPBundle;
import org.consulo.php.lang.psi.PHPFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.structureView.StructureView;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.ide.structureView.impl.StructureViewComposite;
import com.intellij.ide.structureView.impl.TemplateLanguageStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.psi.PsiFile;

/**
 * @author Maxim.Mossienko
 *         Date: 29.12.2008
 *         Time: 22:56:06
 */
public class PHPStructureViewBuilderProvider implements PsiStructureViewFactory
{
	@Nullable
	public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile)
	{
		return new TemplateLanguageStructureViewBuilder(psiFile)
		{
			protected StructureViewComposite.StructureViewDescriptor createMainView(final FileEditor fileEditor, final PsiFile mainFile)
			{
				StructureView mainView = new TreeBasedStructureViewBuilder()
				{
					@NotNull
					public StructureViewModel createStructureViewModel()
					{
						return new PHPStructureViewModel((PHPFile) mainFile);
					}
				}.createStructureView(fileEditor, mainFile.getProject());
				return new StructureViewComposite.StructureViewDescriptor(PHPBundle.message("tab.structureview.view"), mainView, mainFile.getFileType().getIcon());
			}
		};
	}

}
