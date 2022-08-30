package consulo.php.impl.lang.psi.impl;

import consulo.util.lang.ObjectUtil;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ExtendsList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author jay
 * @date Jun 7, 2008 7:07:36 PM
 */
public class PhpExtendsListImpl extends PhpElementImpl implements ExtendsList
{
	public PhpExtendsListImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@RequiredReadAction
	@Override
	@Nullable
	public PhpClass getExtendsClass()
	{
		ClassReference reference = findChildByClass(ClassReference.class);
		if(reference != null)
		{
			return ObjectUtil.tryCast(reference.resolve(), PhpClass.class);
		}
		return null;
	}
}
