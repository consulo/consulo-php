package org.consulo.php.lang.psi.impl;

import java.util.ArrayList;

import org.consulo.php.lang.PhpFileType;
import org.consulo.php.lang.PhpLanguage;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpField;
import org.consulo.php.lang.psi.PhpFile;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.consulo.php.lang.psi.visitors.PhpRecursiveElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.ArrayUtil;
import lombok.val;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PhpFileImpl extends PsiFileBase implements PhpFile
{
	public PhpFileImpl(FileViewProvider viewProvider)
	{
		super(viewProvider, PhpLanguage.INSTANCE);
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
		return ItemPresentationProviders.getItemPresentation(this);
	}

	@NotNull
	@Override
	public PhpElement[] getTopLevelElements()
	{
		val phpElementList = new ArrayList<PhpElement>();
		accept(new PhpRecursiveElementVisitor()
		{
			@Override
			public void visitClass(PhpClass phpClass)
			{
				if(phpClass.getParent().getParent() == PhpFileImpl.this)
				{
					phpElementList.add(phpClass);
				}
			}

			@Override
			public void visitField(PhpField phpField)
			{
				if(phpField.getParent().getParent() == PhpFileImpl.this)
				{
					phpElementList.add(phpField);
				}
			}

			@Override
			public void visitFunction(PhpFunction phpFunction)
			{
				if(phpFunction.getParent().getParent() == PhpFileImpl.this)
				{
					phpElementList.add(phpFunction);
				}
			}
		});
		return ArrayUtil.toObjectArray(phpElementList, PhpElement.class);
	}
}
