package consulo.php.impl.lang.psi.impl;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.Statement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;

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

	/**
	 * The declarations of the expression of the statement - like the variable of {@code $var = ...;}
	 */
	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		for(PsiElement child = getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(child != lastParent && child instanceof PhpPsiElement && !child.processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}
}
