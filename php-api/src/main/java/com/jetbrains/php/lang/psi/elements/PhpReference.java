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

import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Collection;

public interface PhpReference extends PhpExpression, PhpTypedElement, PsiPolyVariantReference
{
	Condition<PsiElement> INSTANCEOF = e -> e instanceof PhpReference;

	@Nullable
	String getName();

	@Nullable
	default CharSequence getNameCS()
	{
		String name = getName();
		if(name == null)
		{
			return null;
		}

		if(name.length() > 0 && name.charAt(0) == '$')
		{
			return name.substring(1, name.length());
		}
		return name;
	}

	@Nullable
	ASTNode getNameNode();

	/**
	 * Find targets in local file
	 *
	 * @return targets
	 */
	@Nonnull
	Collection<? extends PhpNamedElement> resolveLocal();

	@Nonnull
	PhpType resolveLocalType();

	/**
	 * Find targets in all files
	 *
	 * @param incompleteCode called from code completion
	 * @return targets
	 */
	@Nonnull
	Collection<? extends PhpNamedElement> resolveGlobal(boolean incompleteCode);

	@Nonnull
	String getSignature();

	/**
	 * @return computed namespace
	 */
	@Nonnull
	String getNamespaceName();

	/**
	 * @return namespace explicitly written in current reference
	 */
	@Nonnull
	String getImmediateNamespaceName();

	/**
	 * @return true if reference locally defined as absolute - starts with \ or is always absolute by context
	 */
	boolean isAbsolute();

	@Nullable
	String getFQN();
}
