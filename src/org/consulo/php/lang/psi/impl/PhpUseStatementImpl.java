package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import org.consulo.php.lang.psi.PhpClassReference;
import org.consulo.php.lang.psi.PhpUseStatement;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpUseStatementImpl extends PhpElementImpl implements PhpUseStatement {
	public PhpUseStatementImpl(ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitUseStatement(this);
	}

	@NotNull
	@Override
	public PhpClassReference[] getClassReferences() {
		return findChildrenByClass(PhpClassReference.class);
	}
}
