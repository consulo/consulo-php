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

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.util.ArrayFactory;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotation.access.RequiredReadAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Parameter extends PhpNamedElement, RWAccess, StubBasedPsiElement<PhpParameterStub>
{
	Parameter[] EMPTY_ARRAY = new Parameter[0];
	/**
	 * The empty array of PSI parameters which can be reused to avoid unnecessary allocations.
	 */
	ArrayFactory<Parameter> ARRAY_FACTORY = count -> count == 0 ? EMPTY_ARRAY : new Parameter[count];

	Condition<PsiElement> INSTANCEOF = use -> use instanceof Parameter;

	boolean isOptional();

	@RequiredReadAction
	boolean isVariadic();

	@Override
	@Nonnull
	PhpType getDeclaredType();

	@Nonnull
	PhpType getLocalType();

	/**
	 * Warning: tree materialisation!
	 */
	@Nullable
	PsiElement getDefaultValue();

	@Nullable
	String getDefaultValuePresentation();

	boolean isPassByRef();

	//@Nullable
	//PhpDocParamTag getDocTag();

	@Override
	@Nonnull
	String getName();
}
