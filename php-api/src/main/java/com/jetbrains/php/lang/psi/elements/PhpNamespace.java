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

import com.jetbrains.php.lang.psi.stubs.PhpNamespaceStub;
import consulo.language.psi.PsiElement;
import consulo.language.psi.StubBasedPsiElement;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface PhpNamespace extends PhpNamedElement, StubBasedPsiElement<PhpNamespaceStub>//, PhpScopeHolder, PsiDirectoryContainer
{
	Condition<PsiElement> INSTANCEOF = use -> use instanceof PhpNamespace;

	@Nullable
	GroupStatement getStatements();

	@Nonnull
	default String getParentNamespaceName()
	{
		// TODO [VISTALL] impl this
		return "";
	}

	boolean isBraced();
}
