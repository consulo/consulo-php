package consulo.php.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import consulo.php.lang.PhpFileType;
import consulo.php.lang.psi.PhpClass;
import consulo.php.lang.psi.PhpElement;
import consulo.php.lang.psi.PhpFile;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;

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
		List<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>(list.size());
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
	@Override
	public Object getData(Collection<AbstractTreeNode> abstractTreeNodes, String s)
	{
		return null;
	}

	public PhpClass findSingleClass(Object o)
	{
		if(o instanceof PhpFile)
		{
			PhpFile file = (PhpFile) o;
			FileType fileType = file.getFileType();
			if(fileType == PhpFileType.INSTANCE)
			{
				PhpElement[] topLevelElements = file.getTopLevelElements();
				return topLevelElements.length == 1 && topLevelElements[0] instanceof PhpClass ? (PhpClass) topLevelElements[0] : null;
			}
		}
		return null;
	}
}
