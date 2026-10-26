package consulo.php.impl.lang.psi.impl;

import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.PsiComment;
import org.jspecify.annotations.Nullable;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import jakarta.annotation.Nonnull;

import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.PhpUnaryExpression;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;

/**
 * @author jay
 * @date Apr 15, 2008 9:10:04 PM
 */
public class PhpUnaryExpressionImpl extends PhpElementImpl implements PhpUnaryExpression
{
	public PhpUnaryExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PhpPsiElement getValue()
	{
		return PhpPsiChildren.phpChild(this, 0);
	}

	@Nullable
	@Override
	public PsiElement getOperation()
	{
		for(PsiElement child = getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(!(child instanceof PhpPsiElement) && !(child instanceof PsiWhiteSpace) && !(child instanceof PsiComment) && child.getTextLength() > 0)
			{
				return child;
			}
		}
		return null;
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitUnaryExpression(this);
	}

	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		PhpPsiElement value = getValue();
		if(value != null && value != lastParent)
		{
			if(!value.processDeclarations(processor, state, null, source))
			{
				return false;
			}
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}

}
