package consulo.php.impl.lang.psi.impl;

import javax.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.Statement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.ast.ASTNode;

/**
 * Created by IntelliJ IDEA.
 * User: jay
 * Date: 30.03.2007
 *
 * @author jay
 */
public class PhpStatementImpl extends PhpElementImpl implements Statement
{

	public PhpStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitStatement(this);
	}
}
