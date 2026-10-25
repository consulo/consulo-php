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

package com.jetbrains.php.lang.psi.elements;

import com.jetbrains.php.lang.psi.stubs.indexes.expectedArguments.PhpExpectedFunctionArgument;
import consulo.language.psi.PsiElement;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * {@code Name(args)} inside {@code #[...]}.
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public interface PhpAttribute extends PhpPsiElement
{
	/**
	 * Argument with the constructor parameter it binds to: the name of a named argument,
	 * or of the attribute constructor parameter at its position
	 */
	final class PhpAttributeArgument
	{
		private final String myName;
		private final int myIndex;
		private final PhpExpectedFunctionArgument myArgument;
		private final boolean myNamed;

		public PhpAttributeArgument(@Nonnull String name, int index, @Nonnull PhpExpectedFunctionArgument argument, boolean named)
		{
			myName = name;
			myIndex = index;
			myArgument = argument;
			myNamed = named;
		}

		/**
		 * @return parameter name without {@code $}, or empty if it can not be determined
		 */
		@Nonnull
		public String getName()
		{
			return myName;
		}

		public int getIndex()
		{
			return myIndex;
		}

		@Nonnull
		public PhpExpectedFunctionArgument getArgument()
		{
			return myArgument;
		}

		public boolean isNamedArgument()
		{
			return myNamed;
		}
	}

	/**
	 * @return attribute class FQN with the leading backslash, resolved against the namespace and {@code use} imports
	 */
	@Nullable
	String getFQN();

	@Nullable
	String getName();

	@Nullable
	ClassReference getClassReference();

	@Nonnull
	List<PhpAttributeArgument> getArguments();

	/**
	 * @return argument values, named arguments unwrapped
	 */
	@Nonnull
	PsiElement[] getParameters();

	/**
	 * @return the declaration this attribute belongs to
	 */
	@Nullable
	PhpAttributesOwner getOwner();
}
