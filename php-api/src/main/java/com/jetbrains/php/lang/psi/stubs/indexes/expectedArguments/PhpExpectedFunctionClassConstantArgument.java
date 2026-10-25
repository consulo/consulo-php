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
 * {@code Foo::BAR} or {@code Foo::class}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public class PhpExpectedFunctionClassConstantArgument extends PhpExpectedFunctionArgument
{
	private final String myClassFqn;
	private final String myConstantName;

	public PhpExpectedFunctionClassConstantArgument(int argumentIndex, @Nonnull String value, @Nonnull String classFqn, @Nonnull String constantName)
	{
		super(argumentIndex, value);
		myClassFqn = classFqn;
		myConstantName = constantName;
	}

	/**
	 * @return class FQN with the leading backslash
	 */
	@Nonnull
	public String getClassFqn()
	{
		return myClassFqn;
	}

	/**
	 * @return constant name, {@code class} for {@code Foo::class}
	 */
	@Nonnull
	public String getConstantName()
	{
		return myConstantName;
	}
}
