package net.jay.plugins.php.lang.psi.visitors;

import java.util.List;

import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author spLeaner
 */
public abstract class PhpRecursiveElementVisitor extends PHPElementVisitor
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
