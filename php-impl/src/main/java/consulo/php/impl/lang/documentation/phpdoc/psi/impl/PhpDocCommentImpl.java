package consulo.php.impl.lang.documentation.phpdoc.psi.impl;

import consulo.language.ast.IElementType;
import consulo.php.impl.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import org.jetbrains.annotations.NonNls;
import consulo.language.psi.PsiElement;
import consulo.language.impl.psi.CompositePsiElement;
import consulo.language.psi.util.PsiTreeUtil;

/**
 * @author jay
 * @date Jun 29, 2008 12:27:19 AM
 */
public class PhpDocCommentImpl extends CompositePsiElement implements PhpDocComment
{

	public PhpDocCommentImpl()
	{
		super(PhpDocElementTypes.DOC_COMMENT);
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
