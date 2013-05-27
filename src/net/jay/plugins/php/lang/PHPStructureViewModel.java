package net.jay.plugins.php.lang;

import net.jay.plugins.php.lang.psi.PHPFile;

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
class PHPStructureViewModel extends TextEditorBasedStructureViewModel
{
	private PHPFile mainFile;
	private Class[] myClasses = {PsiNamedElement.class};

	public PHPStructureViewModel(PHPFile mainFile)
	{
		super(mainFile);
		this.mainFile = mainFile;
	}

	protected PsiFile getPsiFile()
	{
		return mainFile;
	}

	@NotNull
	public StructureViewTreeElement getRoot()
	{
		return new PHPTreeElement(mainFile);
	}

	@NotNull
	public Grouper[] getGroupers()
	{
		return Grouper.EMPTY_ARRAY;
	}

	@NotNull
	public Sorter[] getSorters()
	{
		return new Sorter[]{Sorter.ALPHA_SORTER};
	}

	@NotNull
	public Filter[] getFilters()
	{
		return Filter.EMPTY_ARRAY;
	}

	@NotNull
	protected Class[] getSuitableClasses()
	{
		return myClasses;
	}
}
