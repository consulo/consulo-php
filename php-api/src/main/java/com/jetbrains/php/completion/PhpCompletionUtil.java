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


import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.AutoPopupController;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Completion helpers, in the shape of PhpStorm's {@code PhpCompletionUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpCompletionUtil
{
	private PhpCompletionUtil()
	{
	}

	/**
	 * Opens the completion popup again after an insert, like after {@code Foo::}
	 */
	public static void showCompletion(@Nonnull InsertionContext context)
	{
		AutoPopupController.getInstance(context.getProject()).scheduleAutoPopup(context.getEditor());
	}

	/**
	 * Adds the classes and interfaces whose short names match the prefix matcher of the result set
	 */
	@RequiredReadAction
	@SuppressWarnings("rawtypes")
	public static void addClasses(@Nonnull String prefix,
								  @Nonnull CompletionResultSet result,
								  @Nonnull PhpIndex phpIndex,
								  @Nullable InsertHandler insertHandler)
	{
		CompletionResultSet resultSet = result.withPrefixMatcher(prefix);
		for(String name : phpIndex.getAllClassNames(resultSet.getPrefixMatcher()))
		{
			for(PhpClass phpClass : phpIndex.getClassesByName(name))
			{
				resultSet.addElement(new PhpClassLookupElement(phpClass, true, insertHandler));
			}
		}
	}

	/**
	 * @param namespaceFqn namespace with the leading backslash: {@code \App\Entity}
	 * @return classes directly in the namespace
	 */
	@Nonnull
	@RequiredReadAction
	public static List<PhpClass> filterByNamespace(@Nonnull Collection<PhpClass> classes, @Nonnull String namespaceFqn)
	{
		String namespace = PhpLangUtil.toFQN(namespaceFqn);
		String expected = namespace.endsWith("\\") ? namespace : namespace + "\\";
		List<PhpClass> result = new ArrayList<>();
		for(PhpClass phpClass : classes)
		{
			if(PhpLangUtil.equalsNamespaceNames(PhpLangUtil.getParentNamespaceFQN(phpClass.getFQN()), expected))
			{
				result.add(phpClass);
			}
		}
		return result;
	}
}
