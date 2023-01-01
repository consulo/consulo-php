package consulo.php.impl.ide.projectView;

import consulo.project.ui.view.tree.AbstractTreeNode;
import consulo.project.ui.view.tree.ViewSettings;
import consulo.ui.ex.tree.PresentationData;
import consulo.project.ui.view.tree.AbstractPsiBasedNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

import javax.annotation.Nullable;
import java.util.*;

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
	public boolean canRepresent(Object element)
	{
		if(super.canRepresent(element))
		{
			return true;
		}

		if(element instanceof PsiElement)
		{
			PsiFile containingFile = getValue().getContainingFile();
			return Objects.equals(containingFile, ((PsiElement) element).getContainingFile());
		}
		return false;
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
