package org.consulo.php.lang.psi.impl;

import java.util.List;

import org.consulo.php.lang.PhpFileType;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpElement;
import org.consulo.php.lang.psi.PhpField;
import org.consulo.php.lang.psi.PhpFile;
import org.consulo.php.lang.psi.PhpFunction;
import org.consulo.php.lang.psi.PhpGroup;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.consulo.php.util.PhpPresentationUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.SmartList;

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

	@NotNull
	@Override
	public PhpClass[] getClasses()
	{
		PhpGroup[] groups = getGroups();
		if(groups.length == 0)
		{
			return PhpClass.EMPTY_ARRAY;
		}
		List<PhpClass> list = new SmartList<PhpClass>();
		for(PhpGroup group : groups)
		{
			for(PsiElement psiElement : group.getStatements())
			{
				if(psiElement instanceof PhpClass)
				{
					list.add((PhpClass) psiElement);
				}
			}
		}
		return list.isEmpty() ? PhpClass.EMPTY_ARRAY : list.toArray(new PhpClass[list.size()]);
	}

	@NotNull
	@Override
	public PhpFunction[] getFunctions()
	{
		PhpGroup[] groups = getGroups();
		if(groups.length == 0)
		{
			return PhpFunction.EMPTY_ARRAY;
		}
		List<PhpFunction> list = new SmartList<PhpFunction>();
		for(PhpGroup group : groups)
		{
			for(PsiElement psiElement : group.getStatements())
			{
				if(psiElement instanceof PhpFunction)
				{
					list.add((PhpFunction) psiElement);
				}
			}
		}
		return list.isEmpty() ? PhpFunction.EMPTY_ARRAY : list.toArray(new PhpFunction[list.size()]);
	}

	@NotNull
	@Override
	public PhpField[] getFields()
	{
		PhpGroup[] groups = getGroups();
		if(groups.length == 0)
		{
			return PhpField.EMPTY_ARRAY;
		}
		List<PhpField> list = new SmartList<PhpField>();
		for(PhpGroup group : groups)
		{
			for(PsiElement psiElement : group.getStatements())
			{
				if(psiElement instanceof PhpField)
				{
					list.add((PhpField) psiElement);
				}
			}
		}
		return list.isEmpty() ? PhpField.EMPTY_ARRAY : list.toArray(new PhpField[list.size()]);
	}

	@NotNull
	@Override
	public PhpGroup[] getGroups()
	{
		return findChildrenByClass(PhpGroup.class);
	}
}
