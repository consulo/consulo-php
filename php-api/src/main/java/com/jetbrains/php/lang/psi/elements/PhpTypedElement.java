// Copyright 2000-2018 JetBrains s.r.o.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.jetbrains.php.lang.psi.elements;

import jakarta.annotation.Nonnull;

import com.jetbrains.php.lang.psi.resolve.types.PhpType;

public interface PhpTypedElement extends PhpPsiElement
{
	/**
	 * Evaluate final merged type from code, docs, everything
	 * NOT visiting other files. Safe for stubs, etc.
	 */
	@Nonnull
	PhpType getType();

	/**
	 * Type declared in real code - function return type, parameter type
	 */
	@Nonnull
	default PhpType getDeclaredType()
	{
		return getType();
	}

	/**
	 * Type from PhpDoc - function return type, parameter type, variable or field type
	 */
	@Nonnull
	default PhpType getDocType()
	{
		return getType();
	}

	/**
	 * Type inferred from code - function return type, parameter hints
	 */
	@Nonnull
	default PhpType getInferredType()
	{
		return getType();
	}
}
