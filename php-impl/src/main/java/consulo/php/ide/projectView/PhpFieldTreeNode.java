package consulo.php.ide.projectView;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import com.jetbrains.php.lang.psi.elements.Field;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFieldTreeNode extends AbstractPsiBasedNode<Field>
{
	public PhpFieldTreeNode(Field phpField, ViewSettings viewSettings)
	{
		super(phpField.getProject(), phpField, viewSettings);
	}

	@Override
	public int getWeight()
	{
		return 200;
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
		return Collections.emptyList();
	}

	@Override
	protected void updateImpl(PresentationData presentationData)
	{
		Field value = getValue();

		presentationData.setPresentableText(value.getName());
	}
}
