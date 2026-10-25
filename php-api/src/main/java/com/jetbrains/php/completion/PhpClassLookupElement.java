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


import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.completion.lookup.InsertHandler;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Lookup element of a class, interface, trait or enum, with its namespace as tail text.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpClassLookupElement extends PhpLookupElement
{
	@RequiredReadAction
	@SuppressWarnings("rawtypes")
	public PhpClassLookupElement(@Nonnull PhpClass phpClass, boolean showNamespace, @Nullable InsertHandler insertHandler)
	{
		super(phpClass);
		handler = insertHandler;
		if(showNamespace)
		{
			String namespace = PhpLangUtil.toPresentableFQN(PhpLangUtil.getParentNamespaceFQN(phpClass.getFQN()));
			tailText = namespace.isEmpty() ? null : " (" + namespace.substring(0, namespace.length() - 1) + ")";
		}
	}

	@Nonnull
	public PhpClass getPhpClass()
	{
		return (PhpClass) getObject();
	}
}
