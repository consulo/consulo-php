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

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.Processor;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;

public interface PhpNamedElement extends PsiNameIdentifierOwner, PhpPsiElement, PhpTypedElement
{
	String PS_UNRESERVE_PREFIX = "PS_UNRESERVE_PREFIX_";
	Condition<PsiElement> INSTANCEOF = use -> use instanceof PhpNamedElement;

	@Nullable
	ASTNode getNameNode();

	@Override
	@Nonnull
	@NonNls
	String getName();

	@Nonnull
	@NonNls
	CharSequence getNameCS();

	/**
	 * Doc Comment attached directly to this element, in SAME file
	 * In most of the cases a stub is returned and a tree remains unmaterialized.
	 */
	@Nullable
	PhpDocComment getDocComment();

	/**
	 * process inherited from super or provided by other mechanism
	 *
	 * @param processor Processor
	 */
	void processDocs(Processor<PhpDocComment> processor);

	Set<? extends PhpNamedElement> EMPTY_SET = Collections.emptySet();

	@Nonnull
	String getFQN();

	@Nonnull
	String getNamespaceName();

	boolean isDeprecated();

	boolean isInternal();
}
