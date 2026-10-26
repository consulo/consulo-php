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
import org.jspecify.annotations.Nullable;

/**
 * Reference to a class member: {@code $object->method()}, {@code $object->field}, {@code Foo::CONSTANT}, {@code parent::foo()}
 *
 * @author VISTALL
 * @since 2026-10-26
 */
public interface MemberReference extends PhpReference
{
	/**
	 * @return the qualifier before {@code ->} or {@code ::}: a variable, call, class reference, ...
	 */
	@Nullable
	@RequiredReadAction
	PhpExpression getClassReference();

	/**
	 * @return {@link PhpModifier.State#PARENT} for {@code parent::}, {@link PhpModifier.State#STATIC} for other {@code ::} access,
	 * {@link PhpModifier.State#DYNAMIC} for {@code ->}
	 */
	@RequiredReadAction
	PhpModifier.State getReferenceType();
}
