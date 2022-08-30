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

import consulo.language.psi.PsiElement;
import consulo.util.collection.ArrayFactory;
import consulo.util.lang.function.Condition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * This is actually a method reference.
 * Should be a method definition proxy
 * Name is alias.
 */
public interface PhpTraitUseRule extends PhpNamedElement
{

	PhpTraitUseRule[] EMPTY = new PhpTraitUseRule[0];
	ArrayFactory<PhpTraitUseRule> ARRAY_FACTORY = count -> count == 0 ? EMPTY : new PhpTraitUseRule[count];

	Condition<PsiElement> INSTANCEOF = use -> use instanceof PhpTraitUseRule;

	@Nullable
	Method getOriginal();

	@Nullable
	MethodReference getOriginalReference();

	@Nullable
	ClassReference getOverride();

	@Nullable
	String getAlias();

	boolean isInsteadOf();

	List<Method> getMethods();

	@Nonnull
	PhpModifier.Access getAccess();

	String[] getTraitNames();
}
