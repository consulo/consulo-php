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

import jakarta.annotation.Nonnull;

/**
 * String, number or {@code true}/{@code false}/{@code null}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpExpectedFunctionScalarArgument extends PhpExpectedFunctionArgument
{
	public PhpExpectedFunctionScalarArgument(int argumentIndex, @Nonnull String value)
	{
		super(argumentIndex, value);
	}

	/**
	 * @return string contents without quotes, other scalars as written
	 */
	@Nonnull
	public String getNormalizedValue()
	{
		String value = getValue();
		if(value.length() >= 2)
		{
			char first = value.charAt(0);
			char last = value.charAt(value.length() - 1);
			if((first == '\'' || first == '"') && first == last)
			{
				return value.substring(1, value.length() - 1);
			}
		}
		return value;
	}
}
