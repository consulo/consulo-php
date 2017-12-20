package consulo.php.lang;

import consulo.php.lang.psi.impl.PhpFileImpl;
import org.jetbrains.annotations.NotNull;
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
	@NotNull
	public StructureViewTreeElement getRoot()
	{
		return new PhpTreeElement(mainFile);
	}

	@Override
	@NotNull
	public Grouper[] getGroupers()
	{
		return Grouper.EMPTY_ARRAY;
	}

	@Override
	@NotNull
	public Sorter[] getSorters()
	{
		return new Sorter[]{Sorter.ALPHA_SORTER};
	}

	@Override
	@NotNull
	public Filter[] getFilters()
	{
		return Filter.EMPTY_ARRAY;
	}

	@Override
	@NotNull
	protected Class[] getSuitableClasses()
	{
		return myClasses;
	}
}
