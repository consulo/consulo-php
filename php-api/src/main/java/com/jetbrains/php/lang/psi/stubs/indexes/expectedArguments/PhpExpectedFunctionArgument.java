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

package com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments;

import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.project.Project;
import jakarta.annotation.Nonnull;

/**
 * Value of a call or attribute argument, independent of the PSI.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public abstract class PhpExpectedFunctionArgument
{
	private final int myArgumentIndex;
	private final String myValue;

	protected PhpExpectedFunctionArgument(int argumentIndex, @Nonnull String value)
	{
		myArgumentIndex = argumentIndex;
		myValue = value;
	}

	public int getArgumentIndex()
	{
		return myArgumentIndex;
	}

	/**
	 * @return source text of the argument
	 */
	@Nonnull
	public String getValue()
	{
		return myValue;
	}

	/**
	 * @return PSI of the argument value, parsed out of any file: a string literal, class constant reference, ...
	 */
	@Nonnull
	@RequiredReadAction
	public PsiElement createReference(@Nonnull Project project)
	{
		PhpExpression expression = PhpPsiElementFactory.createFirstFromText(project,
			PhpExpression.class, myValue + ";");
		if(expression == null)
		{
			throw new AssertionError("Not an expression: " + myValue);
		}
		return expression;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "(" + myArgumentIndex + ", " + myValue + ")";
	}
}
