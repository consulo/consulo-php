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

package com.jetbrains.php;


import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Text presentation of PHP elements, in the shape of PhpStorm's {@code PhpPresentationUtil}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public final class PhpPresentationUtil
{
	private PhpPresentationUtil()
	{
	}

	/**
	 * @param context element the parameters are shown for, may be null
	 * @return {@code (string $name, int ...$ids)}
	 */
	@Nonnull
	@RequiredReadAction
	public static CharSequence formatParameters(@Nullable PsiElement context, @Nonnull Parameter[] parameters)
	{
		StringBuilder builder = new StringBuilder("(");
		for(int i = 0; i < parameters.length; i++)
		{
			if(i > 0)
			{
				builder.append(", ");
			}
			Parameter parameter = parameters[i];
			PhpType type = parameter.getDeclaredType();
			if(!type.isEmpty())
			{
				builder.append(type.toStringResolved()).append(' ');
			}
			if(parameter.isVariadic())
			{
				builder.append("...");
			}
			builder.append('$').append(parameter.getName());
		}
		return builder.append(')');
	}
}
