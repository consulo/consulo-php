package consulo.php.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Function;
import javax.annotation.Nullable;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFileTreeNode extends AbstractPsiBasedNode<PhpFile>
{
	public static List<AbstractTreeNode> fillToTreeNodes(PhpPsiElement[] elements, ViewSettings settings)
	{
		List<AbstractTreeNode> list = new ArrayList<AbstractTreeNode>(elements.length);
		for(PhpPsiElement element : elements)
		{
			if(element instanceof Field)
			{
				list.add(new PhpFieldTreeNode((Field) element, settings));
			}
			else if(element instanceof PhpClass)
			{
				list.add(new PhpClassTreeNode((PhpClass) element, settings));
			}
			else if(element instanceof Function)
			{
				list.add(new PhpFunctionTreeNode((Function) element, settings));
			}
		}
		return list.isEmpty() ? Collections.<AbstractTreeNode>emptyList() : list;
	}

	public PhpFileTreeNode(PhpFile phpFile, ViewSettings viewSettings)
	{
		super(phpFile.getProject(), phpFile, viewSettings);
	}

	@Override
	public int getWeight()
	{
		return 0;
	}

	@Override
	public boolean expandOnDoubleClick()
	{
		return false;
	}

	@Nullable
	@Override
	protected com.intellij.psi.PsiElement extractPsiFromValue()
	{
		return getValue();
	}

	@Nullable
	@Override
	protected Collection<AbstractTreeNode> getChildrenImpl()
	{
		if(!getSettings().isShowMembers())
		{
			return Collections.emptyList();
		}
		PhpFile value = getValue();

		return fillToTreeNodes(value.getTopLevelElements(), getSettings());
	}

	@Override
	protected void updateImpl(PresentationData presentationData)
	{
		presentationData.setPresentableText(getValue().getName());
	}
}
