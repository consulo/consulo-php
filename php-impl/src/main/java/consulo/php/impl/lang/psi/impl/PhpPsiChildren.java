package consulo.php.impl.lang.psi.impl;

import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.ast.TokenSet;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Child lookups of composite PHP elements
 *
 * @author VISTALL
 * @since 2026-10-26
 */
final class PhpPsiChildren
{
	private PhpPsiChildren()
	{
	}

	static List<PhpPsiElement> phpChildren(PsiElement element)
	{
		List<PhpPsiElement> result = new ArrayList<>();
		for(PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(child instanceof PhpPsiElement phpChild)
			{
				result.add(phpChild);
			}
		}
		return result;
	}

	@Nullable
	static PhpPsiElement phpChild(PsiElement element, int index)
	{
		List<PhpPsiElement> children = phpChildren(element);
		return index >= 0 && index < children.size() ? children.get(index) : null;
	}

	@Nullable
	static PhpPsiElement lastPhpChild(PsiElement element)
	{
		List<PhpPsiElement> children = phpChildren(element);
		return children.isEmpty() ? null : children.get(children.size() - 1);
	}

	/**
	 * @return the first token child of the given types
	 */
	@Nullable
	static PsiElement token(PsiElement element, TokenSet types)
	{
		for(PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if(!(child instanceof PsiWhiteSpace) && !(child instanceof PsiComment) && types.contains(child.getNode().getElementType()))
			{
				return child;
			}
		}
		return null;
	}
}
