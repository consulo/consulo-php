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
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpField;
import consulo.php.lang.psi.PhpFile;
import consulo.php.lang.psi.PhpFunction;
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
	public PhpElement[] getTopLevelElements()
	{
		List<PhpElement> phpElementList = new ArrayList<PhpElement>();
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
