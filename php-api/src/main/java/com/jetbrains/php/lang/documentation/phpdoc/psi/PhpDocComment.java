package com.jetbrains.php.lang.documentation.phpdoc.psi;

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import consulo.language.psi.PsiComment;
import consulo.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocReturnTag;
import consulo.php.lang.documentation.phpdoc.psi.tags.PhpDocVarTag;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * @author jay
 * @date Jun 29, 2008 12:13:59 AM
 */
public interface PhpDocComment extends PsiComment, PhpDocPsiElement
{
	/**
	 * @param name tag name with {@code @}, case-sensitive; top-level tags only
	 */
	@Nonnull
	PhpDocTag[] getTagElementsByName(@Nonnull String name);

	@Nonnull
	PhpDocParamTag[] getParamTags();

	/**
	 * @param name parameter name without {@code $}
	 */
	@Nullable
	PhpDocParamTag getParamTagByName(@Nonnull String name);

	public PhpDocVarTag getVarTag();

	public PhpDocReturnTag getReturnTag();
}
