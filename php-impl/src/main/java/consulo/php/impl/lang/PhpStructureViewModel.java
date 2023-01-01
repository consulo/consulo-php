package consulo.php.impl.lang;

import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.fileEditor.structureView.tree.Filter;
import consulo.fileEditor.structureView.tree.Grouper;
import consulo.fileEditor.structureView.tree.Sorter;
import consulo.language.editor.structureView.TextEditorBasedStructureViewModel;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiNamedElement;
import consulo.php.impl.lang.psi.impl.PhpFileImpl;

import javax.annotation.Nonnull;

/**
 * @author AG
 */
class PhpStructureViewModel extends TextEditorBasedStructureViewModel
{
	private PhpFileImpl mainFile;
	private Class[] myClasses = {PsiNamedElement.class};

	public PhpStructureViewModel(PhpFileImpl mainFile)
	{
		super(mainFile);
		this.mainFile = mainFile;
	}

	@Override
	protected PsiFile getPsiFile()
	{
		return mainFile;
	}

	@Override
	@Nonnull
	public StructureViewTreeElement getRoot()
	{
		return new PhpTreeElement(mainFile);
	}

	@Override
	@Nonnull
	public Grouper[] getGroupers()
	{
		return Grouper.EMPTY_ARRAY;
	}

	@Override
	@Nonnull
	public Sorter[] getSorters()
	{
		return new Sorter[]{Sorter.ALPHA_SORTER};
	}

	@Override
	@Nonnull
	public Filter[] getFilters()
	{
		return Filter.EMPTY_ARRAY;
	}

	@Override
	@Nonnull
	protected Class[] getSuitableClasses()
	{
		return myClasses;
	}
}
