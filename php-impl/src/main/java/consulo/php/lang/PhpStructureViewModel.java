package consulo.php.lang;

import javax.annotation.Nonnull;

import consulo.php.lang.psi.impl.PhpFileImpl;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;

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
