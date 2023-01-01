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

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import consulo.application.util.function.Processor;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.util.lang.StringUtil;
import consulo.util.lang.function.Condition;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	default CharSequence getNameCS()
	{
		String name = getName();
		if(name.length() > 0 && name.charAt(0) == '$')
		{
			return name.substring(1, name.length());
		}
		return name;
	}

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

	@Nonnull
	default String getFQN()
	{
		String namespaceName = getNamespaceName();
		if(StringUtil.isEmpty(namespaceName))
		{
			return "\\" + getName();
		}
		return "\\" + namespaceName + "\\" + getName();
	}

	@Nonnull
	String getNamespaceName();

	boolean isDeprecated();

	boolean isInternal();
}
