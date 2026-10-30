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
package com.jetbrains.php.refactoring.changeSignature;

import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * A parameter of a changed signature, in the shape of PhpStorm's {@code PhpParameterInfo}.
 *
 * @author VISTALL
 * @since 2026-10-30
 */
public class PhpParameterInfo
{
	private final int myOldIndex;
	@Nonnull
	private String myName;
	@Nonnull
	private PhpType myType = PhpType.EMPTY;
	@Nullable
	private String myTypeText;
	@Nullable
	private String myDefaultValue;

	/**
	 * @param oldIndex index of the parameter in the old signature, or the position of a new parameter
	 */
	public PhpParameterInfo(int oldIndex, @Nonnull String name)
	{
		myOldIndex = oldIndex;
		myName = name;
	}

	public int getOldIndex()
	{
		return myOldIndex;
	}

	@Nonnull
	public String getName()
	{
		return myName;
	}

	public void setName(@Nonnull String name)
	{
		myName = name;
	}

	@Nonnull
	public PhpType getType()
	{
		return myType;
	}

	/**
	 * @param typeText the type as written in the signature, like an imported short class name
	 */
	public void setType(@Nonnull PhpType type, @Nullable String typeText)
	{
		myType = type;
		myTypeText = typeText;
	}

	@Nullable
	public String getTypeText()
	{
		return myTypeText;
	}

	@Nullable
	public String getDefaultValue()
	{
		return myDefaultValue;
	}

	public void setDefaultValue(@Nullable String defaultValue)
	{
		myDefaultValue = defaultValue;
	}
}
