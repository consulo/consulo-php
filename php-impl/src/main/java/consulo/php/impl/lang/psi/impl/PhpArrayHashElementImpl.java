package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.parser.PhpElementTypes;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.ast.IElementType;
import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;

import consulo.language.ast.ASTNode;
import com.jetbrains.php.lang.psi.elements.ArrayHashElement;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;
import consulo.util.lang.ObjectUtil;

/**
 * @author VISTALL
 * @since 2019-04-12
 */
public class PhpArrayHashElementImpl extends PhpElementImpl implements ArrayHashElement
{
	public PhpArrayHashElementImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PhpPsiElement getKey()
	{
		return unwrap(PhpElementTypes.ARRAY_KEY);
	}

	@Nullable
	@Override
	public PhpPsiElement getValue()
	{
		return unwrap(PhpElementTypes.ARRAY_VALUE);
	}

	@Nullable
	private PhpPsiElement unwrap(IElementType wrapperType)
	{
		ASTNode wrapper = getNode().findChildByType(wrapperType);
		return wrapper == null ? null : PsiTreeUtil.getChildOfType(wrapper.getPsi(), PhpPsiElement.class);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitArrayHashElement(this);
	}
}
