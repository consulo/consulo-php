package org.consulo.php.lang;

import org.consulo.php.lang.psi.PhpFile;

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
	private PhpFile mainFile;
	private Class[] myClasses = {PsiNamedElement.class};

	public PhpStructureViewModel(PhpFile mainFile)
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
		return new PhpTreeElement(mainFile);
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
