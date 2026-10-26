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
package com.jetbrains.php.codeInsight;

import com.jetbrains.php.codeInsight.controlFlow.PhpControlFlow;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import consulo.language.psi.PsiElement;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public interface PhpScopeHolder extends PhpPsiElement
{
	Condition<PsiElement> INSTANCE_OF = e -> e instanceof PhpScopeHolder;

	/**
	 * Control flow of the statements of this scope, nested functions, closures and classes excluded
	 */
	@Nonnull
	PhpControlFlow getControlFlow();

	@Nonnull
	default Set<CharSequence> getPredefinedVariables()
	{
		return Collections.emptySet();
	}

	//@NotNull
	//PhpScope getScope();
}
