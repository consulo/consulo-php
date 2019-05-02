package consulo.php.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.PhpFileType;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpTreeStructureProvider implements SelectableTreeStructureProvider
{
	@Nullable
	@Override
	public PsiElement getTopLevelElement(PsiElement element)
	{
		return findSingleClass(element);
	}

	@Override
	public Collection<AbstractTreeNode> modify(AbstractTreeNode abstractTreeNode, Collection<AbstractTreeNode> list, ViewSettings viewSettings)
	{
		List<AbstractTreeNode> nodes = new ArrayList<>(list.size());
		for(AbstractTreeNode treeNode : list)
		{
			Object value = treeNode.getValue();
			PhpClass phpClass = findSingleClass(value);
			if(phpClass != null)
			{
				nodes.add(new PhpClassTreeNode(phpClass, viewSettings));
			}
			else if(value instanceof PhpFile)
			{
				nodes.add(new PhpFileTreeNode((PhpFile) value, viewSettings));
			}
			else
			{
				nodes.add(treeNode);
			}
		}
		return nodes;
	}

	@Nullable
	public PhpClass findSingleClass(Object o)
	{
		if(o instanceof PhpFile)
		{
			PhpFile file = (PhpFile) o;
			FileType fileType = file.getFileType();
			if(fileType == PhpFileType.INSTANCE)
			{
				MultiMap<String, PhpNamedElement> topLevelDefs = file.getTopLevelDefs();
				if(topLevelDefs.size() != 1)
				{
					return null;
				}

				Map.Entry<String, Collection<PhpNamedElement>> item = ContainerUtil.getFirstItem(topLevelDefs.entrySet());
				assert item != null;
				Collection<PhpNamedElement> value = item.getValue();
				if(value.size() != 1)
				{
					return null;
				}
				PhpNamedElement element = ContainerUtil.getFirstItem(value);
				if(element!= null && Comparing.equal(file.getContainingFile().getVirtualFile().getNameWithoutExtension(), element.getName()))
				{
					return element instanceof PhpClass ? (PhpClass) element : null;
				}
			}
		}
		return null;
	}
}
