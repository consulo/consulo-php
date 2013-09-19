package org.consulo.php.lang.psi.impl;

import org.consulo.php.lang.psi.resolve.types.PhpType;
import org.consulo.php.lang.psi.resolve.types.PhpTypeAnnotatorVisitor;
import org.consulo.php.lang.psi.resolve.types.PhpTypeOwner;

import org.consulo.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

/**
 * @author jay
 * @date Jun 18, 2008 12:32:13 PM
 */
public class PhpTypeOwnerImpl extends PhpElementImpl implements PhpTypeOwner
{

	public PhpTypeOwnerImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull PhpElementVisitor visitor) {
		visitor.visitPhpElement(this);
	}

	@Override
	@NotNull
	public PhpType getType()
	{
		PhpType type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		if(type == null)
		{
			PhpTypeAnnotatorVisitor.process(this);
		}
		type = getUserData(PhpTypeAnnotatorVisitor.TYPE_KEY);
		assert type != null;
		return type;
	}
}
