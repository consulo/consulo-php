package org.consulo.php.lang.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.consulo.php.lang.PhpFileType;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.consulo.php.util.PhpPresentationUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpFileImpl extends PsiFileBase implements PhpElement
{

	public PhpFileImpl(FileViewProvider viewProvider)
	{
		super(viewProvider, PhpFileType.INSTANCE.getLanguage());
	}

	@Override
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

	@Override
	public PhpElement getNextPsiSibling()
	{
		return null;
	}

	@Override
	public PhpElement getPrevPsiSibling()
	{
		return null;
	}

	@Override
	@NotNull
	public FileType getFileType()
	{
		return PhpFileType.INSTANCE;
	}

	public String toString()
	{
		return "PHP file";
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if(visitor instanceof PhpElementVisitor)
		{
			((PhpElementVisitor) visitor).visitPhpFile(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return PhpPresentationUtil.getFilePresentation(this);
	}
}
