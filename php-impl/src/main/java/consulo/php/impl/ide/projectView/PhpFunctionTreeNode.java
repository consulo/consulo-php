package consulo.php.impl.ide.projectView;

import java.util.Collection;
import java.util.Collections;

import jakarta.annotation.Nullable;

import consulo.project.ui.view.tree.AbstractPsiBasedNode;
import consulo.project.ui.view.tree.AbstractTreeNode;
import consulo.project.ui.view.tree.ViewSettings;
import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.Function;
import consulo.ui.ex.tree.PresentationData;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFunctionTreeNode extends AbstractPsiBasedNode<Function>
{
	public PhpFunctionTreeNode(Function phpFunction, ViewSettings viewSettings)
	{
		super(phpFunction.getProject(), phpFunction, viewSettings);
	}

	@Override
	public int getWeight()
	{
		return 300;
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
		Function value = getValue();

		presentationData.setPresentableText(value.getName());
	}
}
