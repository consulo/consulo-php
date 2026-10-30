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

package com.jetbrains.php.refactoring;

import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpNamespace;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.util.PsiTreeUtil;

import java.util.List;

/**
 * Namespaces of a file, braced ({@code namespace A { }}) or not ({@code namespace A;}).
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpNamespaceBraceConverter
{
	private PhpNamespaceBraceConverter()
	{
	}

	@RequiredReadAction
	public static List<PhpNamespace> getAllNamespaces(PhpFile file)
	{
		return List.copyOf(PsiTreeUtil.findChildrenOfType(file, PhpNamespace.class));
	}
}
