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
package com.jetbrains.php.lang.psi.resolve.types;

import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionAPI;
import consulo.component.extension.ExtensionPointName;
import consulo.language.psi.PsiElement;
import consulo.project.Project;

import jakarta.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * Extension point to implement to provide Type information on various PhpPsiElements.
 *
 * @deprecated use PhpTypeProviderLATEST, this will be dropped in 2019.2
 */
@Deprecated
@ExtensionAPI(ComponentScope.APPLICATION)
public interface PhpTypeProvider3
{
	ExtensionPointName<PhpTypeProvider3> EP_NAME = ExtensionPointName.create(PhpTypeProvider3.class);

	/**
	 * @return Your custom signature key, i.e. "Я". Do not use any of PhpTypeSignatureKey.XXX constants though!
	 */
	char getKey();

	/**
	 * @param element to deduce type for - using only LOCAL info. <b>THIS IS MOST CRUCIAL ASPECT TO FOLLOW</b>
	 * @return type for element, null if no insight. You can return a custom signature here to be later decoded by method below.
	 */
	@Nullable
	PhpType getType(PsiElement element);

	/**
	 * Here you can extend the signature lookups
	 *
	 * @param expression Signature expression to decode. You can use PhpIndex.getBySignature() to look up expression internals.
	 * @param visited    Recursion guard: please pass this on into any phpIndex calls having same parameter
	 * @param depth      Recursion guard: please pass this on into any phpIndex calls having same parameter
	 * @param project    well so you can reach the PhpIndex
	 * @return null if no match
	 */
	Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project);
}
