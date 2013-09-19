package org.consulo.php.ide.projectView;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.AbstractPsiBasedNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import org.consulo.php.lang.psi.PhpFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpFunctionTreeNode extends AbstractPsiBasedNode<PhpFunction> {
	public PhpFunctionTreeNode(PhpFunction phpFunction, ViewSettings viewSettings) {
		super(phpFunction.getProject(), phpFunction, viewSettings);
	}

	@Override
	public int getWeight() {
		return 300;
	}

	@Nullable
	@Override
	protected com.intellij.psi.PsiElement extractPsiFromValue() {
		return getValue();
	}

	@Nullable
	@Override
	protected Collection<AbstractTreeNode> getChildrenImpl() {
		return Collections.emptyList();
	}

	@Override
	protected void updateImpl(PresentationData presentationData) {
		PhpFunction value = getValue();

		presentationData.setPresentableText(value.getName());
	}
}
