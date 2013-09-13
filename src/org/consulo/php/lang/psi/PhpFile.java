package org.consulo.php.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.consulo.php.lang.PhpFileType;
import org.consulo.php.lang.psi.elements.PhpElement;
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
public class PhpFile extends PsiFileBase implements PhpElement
{

	public PhpFile(FileViewProvider viewProvider)
	{
		super(viewProvider, PhpFileType.INSTANCE.getLanguage());
	}

	public PhpElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpElement)
			{
				return (PhpElement) children[0];
			}
		}
		return null;
	}

	public PhpElement getNextPsiSibling()
	{
		return null;
	}

	public PhpElement getPrevPsiSibling()
	{
		return null;
	}

	@NotNull
	public FileType getFileType()
	{
		return PhpFileType.INSTANCE;
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
