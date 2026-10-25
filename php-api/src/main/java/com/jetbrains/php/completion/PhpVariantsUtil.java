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

package com.jetbrains.php.completion;


import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpClassMember;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.completion.insert.PhpReferenceInsertHandler;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lookup elements for PHP elements, in the shape of PhpStorm's {@code PhpVariantsUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpVariantsUtil
{
	private PhpVariantsUtil()
	{
	}

	/**
	 * @param showClassName prefix class members with their class: {@code Foo::BAR}
	 * @param context       element the completion is for, may be null
	 */
	@Nonnull
	@RequiredReadAction
	public static List<LookupElement> getLookupItems(@Nonnull Collection<? extends PhpNamedElement> elements,
													 boolean showClassName,
													 @Nullable PsiElement context)
	{
		List<LookupElement> result = new ArrayList<>(elements.size());
		for(PhpNamedElement element : elements)
		{
			PhpLookupElement lookupElement = element instanceof PhpClass phpClass
				? new PhpClassLookupElement(phpClass, true, PhpReferenceInsertHandler.getInstance())
				: new PhpLookupElement(element);
			if(lookupElement.handler == null)
			{
				lookupElement.handler = PhpReferenceInsertHandler.getInstance();
			}
			if(showClassName && element instanceof PhpClassMember member && member.getContainingClass() != null)
			{
				lookupElement.lookupString = member.getContainingClass().getName() + "::" + lookupElement.lookupString;
			}
			result.add(lookupElement);
		}
		return result;
	}
}
