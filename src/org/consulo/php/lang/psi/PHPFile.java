package org.consulo.php.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.consulo.php.lang.PHPFileType;
import org.consulo.php.lang.psi.elements.PHPPsiElement;
import org.consulo.php.lang.psi.visitors.PHPElementVisitor;
import org.consulo.php.util.PhpPresentationUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPFile extends PsiFileBase implements PHPPsiElement
{

	public PHPFile(FileViewProvider viewProvider)
	{
		super(viewProvider, PHPFileType.INSTANCE.getLanguage());
	}

	public PHPPsiElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PHPPsiElement)
			{
				return (PHPPsiElement) children[0];
			}
		}
		return null;
	}

	public PHPPsiElement getNextPsiSibling()
	{
		return null;
	}

	public PHPPsiElement getPrevPsiSibling()
	{
		return null;
	}

	@NotNull
	public FileType getFileType()
	{
		return PHPFileType.INSTANCE;
	}

	public String toString()
	{
		return "PHP file";
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PHPElementVisitor)
		{
			((PHPElementVisitor) visitor).visitPhpFile(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	public ItemPresentation getPresentation()
	{
		return PhpPresentationUtil.getFilePresentation(this);
	}
}
