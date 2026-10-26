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

import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.stubs.PhpParameterStub;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.language.psi.StubBasedPsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ArrayFactory;
import consulo.util.lang.function.Condition;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface Parameter extends PhpNamedElement, RWAccess, StubBasedPsiElement<PhpParameterStub>, PhpAttributesOwner
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

	/**
	 * @return the written type: {@code Foo} of {@code Foo $a}, null without a type
	 */
	@org.jspecify.annotations.Nullable
	@RequiredReadAction
	default PhpTypeDeclaration getTypeDeclaration()
	{
		return PsiTreeUtil.getChildOfType(this, PhpTypeDeclaration.class);
	}

	/**
	 * @return the {@code @param} tag of this parameter in the doc comment of its function
	 */
	@org.jspecify.annotations.Nullable
	@RequiredReadAction
	default PhpDocParamTag getDocTag()
	{
		Function function = PsiTreeUtil.getParentOfType(this, Function.class);
		PhpDocComment docComment = function == null ? null : function.getDocComment();
		return docComment == null ? null : docComment.getParamTagByName(getName());
	}
}
