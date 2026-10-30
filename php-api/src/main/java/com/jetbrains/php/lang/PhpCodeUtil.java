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

package com.jetbrains.php.lang;

import com.jetbrains.php.lang.psi.PhpCodeEditUtil;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredWriteAction;
import consulo.project.Project;
import org.jspecify.annotations.Nullable;

/**
 * Code generation helpers.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpCodeUtil
{
	private PhpCodeUtil()
	{
	}

	/**
	 * Creates the method from its text and adds it at the end of the class
	 *
	 * @param methodText {@code public function foo() {}}
	 * @return the added method, null when the text is not a method
	 */
	@Nullable
	@RequiredWriteAction
	public static Method createMethodFromTemplate(PhpClass phpClass, Project project, String methodText)
	{
		Method method;
		try
		{
			method = PhpPsiElementFactory.createMethod(project, methodText);
		}
		catch(IllegalArgumentException e)
		{
			return null;
		}
		return PhpCodeEditUtil.insertClassMember(phpClass, method) instanceof Method added ? added : null;
	}
}
