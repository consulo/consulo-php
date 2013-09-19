package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import org.consulo.php.lang.psi.PhpTernaryExpression;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpTernaryExpressionImpl extends PhpElementImpl implements PhpTernaryExpression {
	public PhpTernaryExpressionImpl(ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitTernaryExpression(this);
	}
}
