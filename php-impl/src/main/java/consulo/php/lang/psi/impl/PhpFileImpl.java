package consulo.php.lang.psi.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationProviders;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.containers.MultiMap;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.GroupStatement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.stubs.PhpClassStub;
import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.php.lang.PhpFileType;
import consulo.php.lang.PhpLanguage;
import consulo.php.lang.psi.PhpStubElements;
import consulo.php.lang.psi.visitors.PhpElementVisitor;

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
	public MultiMap<String, PhpNamedElement> getTopLevelDefs()
	{
		StubElement<?> stub = getStub();
		if(stub != null)
		{
			List<StubElement> childrenStubs = stub.getChildrenStubs();
			MultiMap<String, PhpNamedElement> map = new MultiMap<>();
			for(StubElement childrenStub : childrenStubs)
			{
				if(childrenStub instanceof PhpClassStub)
				{
					map.putValue(((PhpClassStub) childrenStub).getName(), ((PhpClassStub) childrenStub).getPsi());
				}
			}
			return map;
		}

		GroupStatement statement = findChildByClass(GroupStatement.class);
		if(statement == null)
		{
			return MultiMap.empty();
		}

		PsiElement[] statements = statement.getStatements();
		MultiMap<String, PhpNamedElement> map = new MultiMap<>();
		for(PsiElement psiElement : statements)
		{
			if(psiElement instanceof PhpClass)
			{
				map.putValue(((PhpClass) psiElement).getName(), (PhpNamedElement) psiElement);
			}
		}
		return map;
	}

	@Override
	public String getMainNamespaceName()
	{
		StubElement<?> stub = getStub();
		if(stub != null)
		{
			PhpNamespaceStub stubElement = (PhpNamespaceStub)stub.findChildStubByType(PhpStubElements.NAMESPACE);
			if(stubElement != null)
			{
				return stubElement.getName();
			}
			return "";
		}

		GroupStatement statement = findChildByClass(GroupStatement.class);
		if(statement == null)
		{
			return "";
		}

		PsiElement[] statements = statement.getStatements();
		MultiMap<String, PhpNamedElement> map = new MultiMap<>();
		for(PsiElement psiElement : statements)
		{
			if(psiElement instanceof PhpNamespace)
			{
				return ((PhpNamespace) psiElement).getName();
			}
		}
		return "";
	}

	@Nonnull
	@Override
	public Set<CharSequence> getPredefinedVariables()
	{
		return Collections.emptySet();
	}
}
