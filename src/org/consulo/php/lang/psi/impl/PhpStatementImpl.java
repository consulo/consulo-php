package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.PhpStatement;

import com.intellij.lang.ASTNode;
import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 30.03.2007
 *
 * @author jay
 */
public class PhpStatementImpl extends PhpElementImpl implements PhpStatement
{

	public PhpStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitStatement(this);
	}
}
