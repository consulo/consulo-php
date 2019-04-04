package consulo.php.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.ArrayUtil;
import consulo.php.lang.PhpFileType;
import consulo.php.lang.PhpLanguage;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Function;
import consulo.php.lang.psi.visitors.PhpElementVisitor;
import consulo.php.lang.psi.visitors.PhpRecursiveElementVisitor;

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
	public PhpPsiElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpPsiElement)
			{
				return (PhpPsiElement) children[0];
			}
		}
		return null;
	}

	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		return null;
	}

	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		return null;
	}

	@Override
	@Nonnull
	public FileType getFileType()
	{
		return PhpFileType.INSTANCE;
	}

	@Override
	public void accept(@Nonnull PsiElementVisitor visitor)
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

	@Nonnull
	@Override
	public PhpPsiElement[] getTopLevelElements()
	{
		List<PhpPsiElement> phpElementList = new ArrayList<PhpPsiElement>();
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
			public void visitField(Field phpField)
			{
				if(phpField.getParent().getParent() == PhpFileImpl.this)
				{
					phpElementList.add(phpField);
				}
			}

			@Override
			public void visitFunction(Function phpFunction)
			{
				if(phpFunction.getParent().getParent() == PhpFileImpl.this)
				{
					phpElementList.add(phpFunction);
				}
			}
		});
		return ArrayUtil.toObjectArray(phpElementList, PhpPsiElement.class);
	}
}
