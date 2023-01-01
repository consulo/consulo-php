package consulo.php.impl.lang.psi.visitors;

import java.util.List;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.file.FileViewProvider;
import consulo.language.psi.PsiRecursiveVisitor;

/**
 * @author spLeaner
 */
public abstract class PhpRecursiveElementVisitor extends PhpElementVisitor implements PsiRecursiveVisitor
{
	private final boolean myVisitAllFileRoots;

	public PhpRecursiveElementVisitor()
	{
		myVisitAllFileRoots = false;
	}

	public PhpRecursiveElementVisitor(final boolean visitAllFileRoots)
	{
		myVisitAllFileRoots = visitAllFileRoots;
	}

	@Override
	public void visitElement(final PsiElement element)
	{
		element.acceptChildren(this);
	}

	@Override
	public void visitFile(final PsiFile file)
	{
		if(myVisitAllFileRoots)
		{
			final FileViewProvider viewProvider = file.getViewProvider();
			final List<PsiFile> allFiles = viewProvider.getAllFiles();
			if(allFiles.size() > 1)
			{
				if(file == viewProvider.getPsi(viewProvider.getBaseLanguage()))
				{
					for(PsiFile lFile : allFiles)
					{
						lFile.acceptChildren(this);
					}
					return;
				}
			}
		}

		super.visitFile(file);
	}
}
