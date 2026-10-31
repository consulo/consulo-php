package consulo.php.impl.lang.psi.impl;

import org.jspecify.annotations.Nullable;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.ArrayIndex;
import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiScopeProcessor;
import consulo.language.psi.resolve.ResolveState;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author VISTALL
 * @since 2019-04-12
 */
public class PhpArrayAccessExpressionImpl extends PhpElementImpl implements ArrayAccessExpression
{
	public PhpArrayAccessExpressionImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitPhpElement(this);
	}

	@Nullable
	@Override
	public PhpPsiElement getValue()
	{
		return PhpPsiChildren.phpChild(this, 0);
	}

	@Nullable
	@Override
	public ArrayIndex getIndex()
	{
		return findChildByClass(ArrayIndex.class);
	}

	/**
	 * Writing an element - {@code $var['key'] = ...} - declares the array variable
	 */
	@Override
	public boolean processDeclarations(@Nonnull PsiScopeProcessor processor, @Nonnull ResolveState state, PsiElement lastParent, @Nonnull PsiElement source)
	{
		PhpPsiElement array = getValue();
		if(array != null && array != lastParent && !array.processDeclarations(processor, state, null, source))
		{
			return false;
		}
		return super.processDeclarations(processor, state, lastParent, source);
	}
}
