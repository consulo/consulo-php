package consulo.php.impl.ide.projectView;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import com.jetbrains.php.lang.psi.elements.Field;
import consulo.language.psi.PsiElement;
import consulo.project.ui.view.tree.AbstractTreeNode;
import consulo.ui.ex.tree.PresentationData;
import consulo.project.ui.view.tree.ViewSettings;
import consulo.project.ui.view.tree.AbstractPsiBasedNode;

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
	protected PsiElement extractPsiFromValue()
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
