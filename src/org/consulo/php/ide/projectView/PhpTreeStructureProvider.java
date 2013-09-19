package org.consulo.php.ide.projectView;

import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;
import org.consulo.php.lang.PhpFileType;
import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpFile;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpTreeStructureProvider implements SelectableTreeStructureProvider {
	@Nullable
	@Override
	public PsiElement getTopLevelElement(PsiElement element) {
		return findSingleClass(element);
	}

	@Override
	public Collection<AbstractTreeNode> modify(AbstractTreeNode abstractTreeNode, Collection<AbstractTreeNode> list, ViewSettings viewSettings) {
		List<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>(list.size());
		for (AbstractTreeNode treeNode : list) {
			Object value = treeNode.getValue();
			PhpClass phpClass = findSingleClass(value);
			if(phpClass != null) {
				nodes.add(new PhpClassTreeNode(phpClass, viewSettings));
			}
			else if (value instanceof PhpFile) {
				nodes.add(new PhpFileTreeNode((PhpFile) value, viewSettings));
			}
			else {
				nodes.add(treeNode);
			}
		}
		return nodes;
	}

	@Nullable
	@Override
	public Object getData(Collection<AbstractTreeNode> abstractTreeNodes, String s) {
		return null;
	}

	public PhpClass findSingleClass(Object o) {
		if (o instanceof PhpFile) {
			PhpFile file = (PhpFile) o;
			FileType fileType = file.getFileType();
			if (fileType == PhpFileType.INSTANCE) {
				boolean b = true;
				PhpClass[] classes = file.getClasses();

				b &= classes.length == 1;
				b &= file.getFields().length == 0;
				b &= file.getFunctions().length == 0;

				return b ? file.getClasses()[0] : null;
			}
		}
		return null;
	}
}
