package org.consulo.php.lang.psi.impl;

import com.intellij.lang.ASTNode;
import org.consulo.php.lang.psi.PhpElvisExpression;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 19.09.13.
 */
public class PhpElvisExpressionImpl extends PhpElementImpl implements PhpElvisExpression {
	public PhpElvisExpressionImpl(ASTNode node) {
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitElvisExpression(this);
	}
}
