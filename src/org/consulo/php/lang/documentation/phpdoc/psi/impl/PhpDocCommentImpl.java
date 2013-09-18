package org.consulo.php.lang.documentation.phpdoc.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import org.consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import org.consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import org.consulo.php.lang.psi.PhpElement;
import org.jetbrains.annotations.NonNls;

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
	public PhpElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PhpElement)
			{
				return (PhpElement) children[0];
			}
		}
		return null;
	}

	@Override
	public PhpElement getNextPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && children.length > i + 1)
			{
				if(children[i + 1] instanceof PhpElement)
				{
					nextSibling = (PhpElement) children[i + 1];
				}
				break;
			}
		}
		return nextSibling;
	}

	@Override
	public PhpElement getPrevPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PhpElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && i > 0)
			{
				if(children[i - 1] instanceof PhpElement)
				{
					prevSibling = (PhpElement) children[i - 1];
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
