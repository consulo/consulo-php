package consulo.php.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFileTreeNode extends AbstractPsiBasedNode<PhpFile>
{
	@Nonnull
	public static List<AbstractTreeNode> fillToTreeNodes(Collection<? extends PhpPsiElement> elements, ViewSettings settings)
	{
		List<AbstractTreeNode> list = new ArrayList<>(elements.size());
		for(PhpPsiElement element : elements)
		{
			AbstractTreeNode<?> node = mapNode(element, settings);
			if(node != null)
			{
				list.add(node);
			}
		}
		return list.isEmpty() ? Collections.emptyList() : list;
	}

	@Nullable
	private static AbstractTreeNode<?> mapNode(Object element, ViewSettings settings)
	{
		if(element instanceof Field)
		{
			return new PhpFieldTreeNode((Field) element, settings);
		}
		else if(element instanceof PhpClass)
		{
			return new PhpClassTreeNode((PhpClass) element, settings);
		}
		else if(element instanceof Function)
		{
			return new PhpFunctionTreeNode((Function) element, settings);
		}

		return null;
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
		PhpFile value = getValue();

		return fillToTreeNodes(value.getTopLevelDefs().values(), getSettings());
	}

	@Override
	protected void updateImpl(PresentationData presentationData)
	{
		presentationData.setPresentableText(getValue().getName());
	}
}
