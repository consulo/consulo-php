/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.php.lang.documentation.phpdoc;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpDocUtil
{
	/**
	 * Standard PHPDoc tags
	 */
	public static final String[] ALL_TAGS = {
		"@abstract", "@access", "@api", "@author", "@category", "@copyright", "@deprecated", "@example", "@filesource", "@final", "@global",
		"@ignore", "@inheritDoc", "@internal", "@license", "@link", "@method", "@mixin", "@name", "@package", "@param", "@property",
		"@property-read", "@property-write", "@return", "@see", "@since", "@static", "@staticvar", "@subpackage", "@throws", "@todo",
		"@tutorial", "@uses", "@used-by", "@var", "@version"
	};

	private PhpDocUtil()
	{
	}

	/**
	 * @param name tag name with {@code @}, {@code null} for all tags; nested annotation tags are included
	 */
	public static void consumeTagElementsByName(@Nonnull PhpDocComment docComment, @Nullable String name, @Nonnull Consumer<? super PhpDocTag> consumer)
	{
		processTagElementsByPredicate(docComment, consumer, tag -> name == null || name.equals(tag.getName()));
	}

	public static void processTagElementsByPredicate(@Nonnull PhpDocComment docComment,
													 @Nonnull Consumer<? super PhpDocTag> consumer,
													 @Nonnull Predicate<? super PhpDocTag> predicate)
	{
		for(PhpDocTag tag : PsiTreeUtil.findChildrenOfType((PsiElement) docComment, PhpDocTag.class))
		{
			if(predicate.test(tag))
			{
				consumer.accept(tag);
			}
		}
	}
}
