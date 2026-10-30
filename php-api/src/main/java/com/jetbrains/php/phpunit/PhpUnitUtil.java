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

package com.jetbrains.php.phpunit;

import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiFile;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * PHPUnit test classes and files.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public final class PhpUnitUtil
{
	public static final String TEST_CASE_FQN = "\\PHPUnit\\Framework\\TestCase";
	public static final String LEGACY_TEST_CASE_FQN = "\\PHPUnit_Framework_TestCase";

	private PhpUnitUtil()
	{
	}

	/**
	 * @return true for a class extending the PHPUnit test case (resolves the parent classes through the index)
	 */
	@RequiredReadAction
	public static boolean isTestClass(@Nullable PhpClass phpClass)
	{
		Set<PhpClass> visited = new HashSet<>();
		for(PhpClass current = phpClass; current != null && visited.add(current); current = current.getSuperClass())
		{
			String fqn = current.getFQN();
			if(PhpLangUtil.equalsClassNames(fqn, TEST_CASE_FQN) || PhpLangUtil.equalsClassNames(fqn, LEGACY_TEST_CASE_FQN))
			{
				return current != phpClass;
			}
		}
		return false;
	}

	/**
	 * @return true for a PHP file declaring a PHPUnit test class
	 */
	@RequiredReadAction
	public static boolean isPhpUnitTestFile(@Nullable PsiFile file)
	{
		if(!(file instanceof PhpFile phpFile))
		{
			return false;
		}
		for(PhpClass phpClass : PhpPsiUtil.findAllClasses(phpFile))
		{
			if(isTestClass(phpClass))
			{
				return true;
			}
		}
		return false;
	}
}
