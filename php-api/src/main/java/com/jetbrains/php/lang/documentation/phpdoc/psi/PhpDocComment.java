package com.jetbrains.php.lang.documentation.phpdoc.psi;

import consulo.language.psi.PsiComment;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;

/**
 * @author jay
 * @date Jun 29, 2008 12:13:59 AM
 */
public interface PhpDocComment extends PsiComment, PhpDocPsiElement
{
	public PhpDocVarTag getVarTag();

	public PhpDocReturnTag getReturnTag();
}
