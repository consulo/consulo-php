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

package com.jetbrains.php.lang.psi.resolve.types;

import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Type aliases of phpstan and psalm: {@code @phpstan-type Options array{...}}, {@code @psalm-type Options = array{...}} and
 * {@code @psalm-import-type Options from Source as Alias}, declared in the doc comment of a class.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpDocTagAliasTypeProvider
{
	public static final String TYPE_TAG_POSTFIX = "-type";
	public static final String IMPORT_TYPE_TAG_POSTFIX = "-import-type";

	private PhpDocTagAliasTypeProvider()
	{
	}

	/**
	 * @return the alias tags of the class owning the type that declare or import its name
	 */
	@RequiredReadAction
	public static Collection<PhpDocTag> resolveTypeAliases(PhpDocType type)
	{
		String name = type.getName();
		PhpClass phpClass = getOwnerClass(type);
		if(name == null || phpClass == null)
		{
			return List.of();
		}
		return resolveLocalTypeFromTypeAlias(name, phpClass.getDocComment());
	}

	/**
	 * @return the alias tags of the doc comment that declare or import the name
	 */
	@RequiredReadAction
	public static Collection<PhpDocTag> resolveLocalTypeFromTypeAlias(String name, @Nullable PhpDocComment docComment)
	{
		if(docComment == null)
		{
			return List.of();
		}
		List<PhpDocTag> result = new ArrayList<>();
		for(PhpDocTag tag : PsiTreeUtil.getChildrenOfTypeAsList(docComment, PhpDocTag.class))
		{
			if(!isAliasTag(tag))
			{
				continue;
			}
			PsiElement identifier = getAliasIdentifier(tag);
			if(identifier != null && name.equals(identifier.getText()))
			{
				result.add(tag);
			}
		}
		return result;
	}

	/**
	 * @return the name the tag makes available: {@code Options} of {@code @psalm-type Options = ...}, {@code Alias} of
	 * {@code @psalm-import-type Options from Source as Alias}
	 */
	@Nullable
	@RequiredReadAction
	public static PsiElement getAliasIdentifier(PhpDocTag tag)
	{
		// identifiers outside of the types, the "as Alias" part may be inside the tag value
		List<PsiElement> identifiers = new ArrayList<>();
		PsiTreeUtil.processElements(tag, element -> {
			if(element.getNode().getElementType() == PhpDocTokenTypes.DOC_IDENTIFIER && PsiTreeUtil.getParentOfType(element, PhpDocType.class, true, PhpDocTag.class) == null)
			{
				identifiers.add(element);
			}
			return true;
		});
		for(int i = 1; i + 1 < identifiers.size(); i++)
		{
			if("as".equals(identifiers.get(i).getText()))
			{
				return identifiers.get(i + 1);
			}
		}
		return identifiers.isEmpty() ? null : identifiers.get(0);
	}

		public static boolean isAliasTag(PhpDocTag tag)
	{
		String name = tag.getName();
		return (name.startsWith("@phpstan-") || name.startsWith("@psalm-") || name.startsWith("@phan-")) && name.endsWith(TYPE_TAG_POSTFIX)
			&& !name.endsWith("-return-type") && !name.endsWith("-var-type");
	}

	@Nullable
	private static PhpClass getOwnerClass(PsiElement element)
	{
		PhpClass phpClass = PsiTreeUtil.getParentOfType(element, PhpClass.class);
		if(phpClass != null)
		{
			return phpClass;
		}
		// the doc comment of a class is its previous sibling
		PhpDocComment docComment = PsiTreeUtil.getParentOfType(element, PhpDocComment.class);
		PsiElement next = docComment == null ? null : PsiTreeUtil.skipWhitespacesForward(docComment);
		return next instanceof PhpClass nextClass ? nextClass : null;
	}
}
