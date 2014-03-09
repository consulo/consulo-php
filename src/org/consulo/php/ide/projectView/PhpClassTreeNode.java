package org.consulo.php.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.consulo.php.lang.psi.PhpClass;
import org.consulo.php.lang.psi.PhpElement;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;

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

		List<PhpElement> list = new ArrayList<PhpElement>();
		Collections.addAll(list, value.getFields());
		Collections.addAll(list, value.getFunctions());
		return PhpFileTreeNode.fillToTreeNodes(ArrayUtil.toObjectArray(list, PhpElement.class), getSettings());
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
