package consulo.php.impl.lang.psi.impl;

import java.util.List;
import org.jspecify.annotations.Nullable;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.PhpAssignmentExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 4, 2008 11:28:27 AM
 */
public class PhpAssignmentExpressionImpl extends PhpTypedElementImpl implements PhpAssignmentExpression
{
	public PhpAssignmentExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PhpPsiElement getVariable()
	{
		return PhpPsiChildren.phpChild(this, 0);
	}

	@Nullable
	@Override
	public PhpPsiElement getValue()
	{
		List<PhpPsiElement> children = PhpPsiChildren.phpChildren(this);
		return children.size() < 2 ? null : children.get(children.size() - 1);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitAssignmentExpression(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState resolveState, PsiElement lastParent, @Nonnull PsiElement source)
	{
		PhpPsiElement value = getValue();
		PhpPsiElement variable = getVariable();
		if(value != null && variable != null && value != lastParent && variable != lastParent)
		{
			if(!value.processDeclarations(processor, resolveState, null, source))
			{
				return false;
			}
			if(!variable.processDeclarations(processor, resolveState, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, resolveState, lastParent, source);
	}

}
