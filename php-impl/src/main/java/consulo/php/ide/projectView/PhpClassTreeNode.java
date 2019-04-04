package consulo.php.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpClassTreeNode extends AbstractPsiBasedNode<PhpClass>
{
	public PhpClassTreeNode(PhpClass phpClass, ViewSettings viewSettings)
	{
		super(phpClass.getProject(), phpClass, viewSettings);
	}

	@Override
	public int getWeight()
	{
		return 100;
	}

	@Nullable
	@Override
	protected PsiElement extractPsiFromValue()
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
		PhpClass value = getValue();

		List<PhpPsiElement> list = new ArrayList<>();
		Collections.addAll(list, value.getOwnFields());
		Collections.addAll(list, value.getOwnMethods());
		return PhpFileTreeNode.fillToTreeNodes(list, getSettings());
	}

	@Override
	public boolean expandOnDoubleClick()
	{
		return false;
	}

	@Override
	protected void updateImpl(PresentationData presentationData)
	{
		PhpClass value = getValue();

		presentationData.setPresentableText(value.getName());
	}
}
