/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetbrains.php.lang.psi.elements;

import consulo.annotation.access.RequiredReadAction;
import consulo.language.psi.PsiElement;
import consulo.util.lang.function.Condition;

import java.util.List;

/**
 * A type written in code: of a parameter, a property ({@link PhpFieldType}), a function ({@link PhpReturnType}), a class constant or the
 * backing type of an enum. {@link #getType()} is the type with class names resolved against the namespace and imports.
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public interface PhpTypeDeclaration extends PhpPsiElement, PhpTypedElement
{
	Condition<PsiElement> INSTANCEOF = e -> e instanceof PhpTypeDeclaration;

	/**
	 * @return the parts of the type, built-in types included: {@code ?Foo} has {@code Foo}, {@code int|Bar} has {@code int} and {@code Bar}
	 */
	@RequiredReadAction
	List<ClassReference> getClassReferences();

	/**
	 * @return true for {@code ?Foo} and unions with {@code null}
	 */
	@RequiredReadAction
	boolean isNullable();
}
