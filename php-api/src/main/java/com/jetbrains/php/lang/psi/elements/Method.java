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

import jakarta.annotation.Nullable;

public interface Method extends PhpClassMember, Function
{
	enum MethodType
	{
		CONSTRUCTOR,
		REGULAR_METHOD,
		UNDEFINED
	}

	Method[] EMPTY = new Method[0];
	ArrayFactory<Method> ARRAY_FACTORY = count -> count == 0 ? EMPTY : new Method[count];
	Condition<PsiElement> INSTANCEOF = use -> use instanceof Method;


	@Nullable
	MethodType getMethodType(boolean allowAmbiguity);

	boolean isStatic();

	boolean isFinal();

	boolean isAbstract();

	default boolean isReturningByReference()
	{
		return false;
	}

	PhpModifier.Access getAccess();
}
