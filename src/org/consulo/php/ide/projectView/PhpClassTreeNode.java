package org.consulo.php.ide.projectView;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.psi.PsiElement;
import org.consulo.php.lang.psi.PhpClass;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpClassTreeNode extends AbstractPsiBasedNode<PhpClass> {
	public PhpClassTreeNode(PhpClass phpClass, ViewSettings viewSettings) {
		super(phpClass.getProject(), phpClass, viewSettings);
	}

	@Override
	public int getWeight() {
		return 100;
	}

	@Nullable
	@Override
	protected PsiElement extractPsiFromValue() {
		return getValue();
	}

	@Nullable
	@Override
	protected Collection<AbstractTreeNode> getChildrenImpl() {
		if(!getSettings().isShowMembers()) {
			return Collections.emptyList();
		}
		PhpClass value = getValue();

		return PhpFileTreeNode.fillToTreeNodes(value, getSettings());
	}

	@Override
	protected void updateImpl(PresentationData presentationData) {
		PhpClass value = getValue();

		presentationData.setPresentableText(value.getName());
	}
}
