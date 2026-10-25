package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.ArrayList;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import consulo.language.ast.IElementType;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import org.jetbrains.annotations.NonNls;
import consulo.language.psi.PsiElement;
import consulo.language.impl.psi.LazyParseablePsiElement;
import consulo.language.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Jun 29, 2008 12:27:19 AM
 */
public class PhpDocCommentImpl extends LazyParseablePsiElement implements PhpDocComment
{

	public PhpDocCommentImpl(CharSequence text)
	{
		super(PhpDocElementTypes.DOC_COMMENT, text);
	}

	@Override
	public IElementType getTokenType()
	{
		return getElementType();
	}

	@Override
	public PhpPsiElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpPsiElement)
			{
				return (PhpPsiElement) children[0];
			}
		}
		return null;
	}

	@Override
	public PhpPsiElement getNextPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpPsiElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && children.length > i + 1)
			{
				if(children[i + 1] instanceof PhpPsiElement)
				{
					nextSibling = (PhpPsiElement) children[i + 1];
				}
				break;
			}
		}
		return nextSibling;
	}

	@Override
	public PhpPsiElement getPrevPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpPsiElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && i > 0)
			{
				if(children[i - 1] instanceof PhpPsiElement)
				{
					prevSibling = (PhpPsiElement) children[i - 1];
				}
				break;
			}
		}
		return prevSibling;
	}

	@NonNls
	public String toString()
	{
		return "PhpDocComment";
	}

	@Nonnull
	@Override
	public PhpDocTag[] getTagElementsByName(@Nonnull String name)
	{
		List<PhpDocTag> result = new ArrayList<>();
		for(PsiElement child : getChildren())
		{
			if(child instanceof PhpDocTag tag && name.equals(tag.getName()))
			{
				result.add(tag);
			}
		}
		return result.toArray(new PhpDocTag[0]);
	}

	@Nonnull
	@Override
	public PhpDocParamTag[] getParamTags()
	{
		PhpDocParamTag[] tags = PsiTreeUtil.getChildrenOfType(this, PhpDocParamTag.class);
		return tags == null ? new PhpDocParamTag[0] : tags;
	}

	@Nullable
	@Override
	public PhpDocParamTag getParamTagByName(@Nonnull String name)
	{
		for(PhpDocParamTag tag : getParamTags())
		{
			if(name.equals(tag.getVarName()))
			{
				return tag;
			}
		}
		return null;
	}

	@Override
	public PhpDocVarTag getVarTag()
	{
		return PsiTreeUtil.getChildOfType(this, PhpDocVarTag.class);
	}

	@Override
	public PhpDocReturnTag getReturnTag()
	{
		return PsiTreeUtil.getChildOfType(this, PhpDocReturnTag.class);
	}
}
