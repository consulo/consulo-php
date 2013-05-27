package net.jay.plugins.php.cache.psi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.jay.plugins.php.cache.PhpFileInfo;
import net.jay.plugins.php.lang.psi.elements.PHPPsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author jay
 * @date May 29, 2008 11:48:18 PM
 */
abstract public class LightPhpElement implements Serializable
{

	private PhpFileInfo fileInfo;
	private LightPhpElement parent;
	private List<LightPhpElement> children = new ArrayList<LightPhpElement>();
	private String name;

	public LightPhpElement()
	{
		fileInfo = null;
		parent = null;
	}

	public LightPhpElement(PhpFileInfo info)
	{
		fileInfo = info;
		this.parent = null;
	}

	public LightPhpElement(LightPhpElement parent)
	{
		fileInfo = parent.getFileInfo();
		this.parent = parent;
	}

	public void registerChild(LightPhpElement child)
	{
	}

	public LightPhpElement(LightPhpElement parent, String name)
	{
		this(parent);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setChildren(List<LightPhpElement> list)
	{
		children = list;
	}

	public List<LightPhpElement> getChildren()
	{
		return children;
	}

	public <ChildType extends LightPhpElement> List<ChildType> getChildrenOfType(Class<ChildType> childType)
	{
		List<ChildType> result = new ArrayList<ChildType>();
		for(LightPhpElement child : children)
		{
			if(childType.isInstance(child))
			{
				//noinspection unchecked
				result.add((ChildType) child);
			}
		}
		return result;
	}

	public int indexOf(LightPhpElement element)
	{
		return children.indexOf(element);
	}

	public boolean addChild(LightPhpElement element)
	{
		return children.add(element);
	}

	public int size()
	{
		return children.size();
	}

	public PhpFileInfo getFileInfo()
	{
		return fileInfo;
	}

	public LightPhpElement getParent()
	{
		return parent;
	}

	public <ParentType extends LightPhpElement> ParentType getParentOfType(Class<ParentType> parentType)
	{
		LightPhpElement parentElement = getParent();
		while(parentElement != null)
		{
			if(parentType.isInstance(parentElement))
			{
				//noinspection unchecked
				return (ParentType) parentElement;
			}
			parentElement = parentElement.getParent();
		}
		return null;
	}

	public VirtualFile getVirtualFile()
	{
		return fileInfo.getVirtualFile();
	}

	@Nullable
	public PHPPsiElement getPsi(Project project)
	{
		return LightElementUtil.findPsiByLightElement(this, project);
	}

	abstract public void accept(@NotNull LightPhpElementVisitor visitor);

}
