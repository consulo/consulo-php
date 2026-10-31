package consulo.php.impl.lang.psi.impl;

import consulo.php.impl.lang.parser.PhpElementTypes;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.ast.IElementType;
import java.util.List;
import java.util.ArrayList;
import jakarta.annotation.Nonnull;

import consulo.language.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.ASTNode;
import consulo.php.impl.lang.psi.visitors.PhpElementVisitor;

/**
 * @author jay
 * @date Apr 15, 2008 2:01:07 PM
 */
public class PhpExpressionParameterListImpl extends PhpElementImpl implements ParameterList
{
	public PhpExpressionParameterListImpl(ASTNode node)
	{
		super(node);
	}

	/**
	 * Argument values: named and spread arguments unwrapped, {@code ...} and {@code ?} placeholders skipped
	 */
	@Nonnull
	@Override
	public PsiElement[] getParameters()
	{
		List<PsiElement> result = new ArrayList<>();
		for(PhpPsiElement child : findChildrenByClass(PhpPsiElement.class))
		{
			IElementType type = child.getNode().getElementType();
			if(type == PhpElementTypes.CALLABLE_PLACEHOLDER)
			{
				continue;
			}
			if(type == PhpElementTypes.VARIADIC_ARGUMENT)
			{
				PhpPsiElement value = PsiTreeUtil.getChildOfType(child, PhpPsiElement.class);
				if(value != null)
				{
					result.add(value);
				}
				continue;
			}
			result.add(child);
		}
		return result.toArray(PsiElement.EMPTY_ARRAY);
	}

	@Override
	public void accept(@Nonnull PhpElementVisitor visitor)
	{
		visitor.visitParameterList(this);
	}
}
