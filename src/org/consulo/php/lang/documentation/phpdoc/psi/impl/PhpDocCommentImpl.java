package org.consulo.php.lang.documentation.phpdoc.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.consulo.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import org.consulo.php.lang.documentation.phpdoc.psi.PhpDocComment;
import org.consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import org.consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import org.consulo.php.lang.psi.elements.PHPPsiElement;
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

	public IElementType getTokenType()
	{
		return getElementType();
	}

	public PHPPsiElement getFirstPsiChild()
	{
		PsiElement[] children = getChildren();
		if(children.length > 0)
		{
			if(children[0] instanceof PHPPsiElement)
			{
				return (PHPPsiElement) children[0];
			}
		}
		return null;
	}

	public PHPPsiElement getNextPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PHPPsiElement nextSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && children.length > i + 1)
			{
				if(children[i + 1] instanceof PHPPsiElement)
				{
					nextSibling = (PHPPsiElement) children[i + 1];
				}
				break;
			}
		}
		return nextSibling;
	}

	public PHPPsiElement getPrevPsiSibling()
	{
		PsiElement[] children = getParent().getChildren();
		PHPPsiElement prevSibling = null;
		for(int i = 0; i < children.length; i++)
		{
			PsiElement child = children[i];
			if(child == this && i > 0)
			{
				if(children[i - 1] instanceof PHPPsiElement)
				{
					prevSibling = (PHPPsiElement) children[i - 1];
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

	public PhpDocVarTag getVarTag()
	{
		return PsiTreeUtil.getChildOfType(this, PhpDocVarTag.class);
	}

	public PhpDocReturnTag getReturnTag()
	{
		return PsiTreeUtil.getChildOfType(this, PhpDocReturnTag.class);
	}
}
