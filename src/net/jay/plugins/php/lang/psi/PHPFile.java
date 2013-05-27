package net.jay.plugins.php.lang.psi;

import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.cache.PhpFileInfo;
import net.jay.plugins.php.cache.psi.LightPhpFile;
import net.jay.plugins.php.lang.PHPFileType;
import net.jay.plugins.php.lang.psi.elements.LightCopyContainer;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;
import net.jay.plugins.php.lang.psi.visitors.PHPElementVisitor;
import net.jay.plugins.php.util.PhpPresentationUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.xml.XmlChildRole;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 26.02.2007
 *
 * @author jay
 */
public class PHPFile extends PsiFileBase implements XmlFile, PHPPsiElement
{

	public PHPFile(FileViewProvider viewProvider)
	{
		super(viewProvider, PHPFileType.PHP.getLanguage());
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

	public List<LightCopyContainer> getChildrenForCache()
	{
		List<LightCopyContainer> elements = new ArrayList<LightCopyContainer>();
		for(PsiElement element : getChildren())
		{
			if(element instanceof LightCopyContainer)
			{
				elements.add((LightCopyContainer) element);
			}
		}
		if(elements.size() > 0)
		{
			return elements;
		}
		for(PsiElement element : getChildren())
		{
			if(element instanceof PHPPsiElement)
			{
				elements.addAll(((PHPPsiElement) element).getChildrenForCache());
			}
		}
		return elements;
	}

	public LightPhpFile getLightCopy(PhpFileInfo info)
	{
		LightPhpFile file = new LightPhpFile(info);
		for(LightCopyContainer container : getChildrenForCache())
		{
			file.addChild(container.getLightCopy(file));
		}
		return file;
	}

	@NotNull
	public FileType getFileType()
	{
		return PHPFileType.PHP;
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

	public XmlDocument getDocument()
	{
		CompositeElement treeElement = calcTreeElement();
	//	ChameleonTransforming.transformChildren(treeElement);
		final PsiElement asPsiElement = treeElement.findChildByRoleAsPsiElement(XmlChildRole.HTML_DOCUMENT);
		if(asPsiElement instanceof XmlDocument)
		{
			return (XmlDocument) asPsiElement;
		}
		return null;
	}

	@Nullable
	@Override
	public XmlTag getRootTag()
	{
		return null;
	}

	public boolean processElements(PsiElementProcessor processor, PsiElement place)
	{
		final XmlDocument document = getDocument();
		return document == null || document.processElements(processor, place);
	}

	public GlobalSearchScope getFileResolveScope()
	{
		return ProjectScope.getAllScope(getProject());
	}

	@Override
	public boolean ignoreReferencedElementAccessibility()
	{
		return false;
	}
}
