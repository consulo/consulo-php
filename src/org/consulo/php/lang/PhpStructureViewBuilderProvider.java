package org.consulo.php.lang;

import org.consulo.php.PhpBundle;
import org.consulo.php.lang.psi.impl.PhpFileImpl;

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
					@NotNull
					public StructureViewModel createStructureViewModel()
					{
						return new PhpStructureViewModel((PhpFileImpl) mainFile);
					}
				}.createStructureView(fileEditor, mainFile.getProject());
				return new StructureViewComposite.StructureViewDescriptor(PhpBundle.message("tab.structureview.view"), mainView, mainFile.getFileType().getIcon());
			}
		};
	}

}
