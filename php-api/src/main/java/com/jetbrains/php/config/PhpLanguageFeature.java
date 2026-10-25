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

package com.jetbrains.php.config;

import jakarta.annotation.Nonnull;

/**
 * Language features with the PHP version which introduced them, see {@link PhpLanguageLevel#hasFeature(PhpLanguageFeature)}
 *
 * @author VISTALL
 * @since 2026-10-25
 */
public enum PhpLanguageFeature
{
	NAMESPACES(PhpLanguageLevel.PHP_5_3),
	CLOSURES(PhpLanguageLevel.PHP_5_3),
	TRAITS(PhpLanguageLevel.PHP_5_4),
	SHORT_ARRAY_SYNTAX(PhpLanguageLevel.PHP_5_4),
	CLASS_NAME_CONST(PhpLanguageLevel.PHP_5_5),
	GENERATORS(PhpLanguageLevel.PHP_5_5),
	FINALLY(PhpLanguageLevel.PHP_5_5),
	VARIADIC_FUNCTIONS(PhpLanguageLevel.PHP_5_6),
	SCALAR_TYPE_HINTS(PhpLanguageLevel.PHP_7_0),
	RETURN_TYPES(PhpLanguageLevel.PHP_7_0),
	COALESCE_OPERATOR(PhpLanguageLevel.PHP_7_0),
	SPACESHIP_OPERATOR(PhpLanguageLevel.PHP_7_0),
	ANONYMOUS_CLASSES(PhpLanguageLevel.PHP_7_0),
	NULLABLES(PhpLanguageLevel.PHP_7_1),
	RETURN_VOID(PhpLanguageLevel.PHP_7_1),
	CLASS_CONSTANT_VISIBILITY(PhpLanguageLevel.PHP_7_1),
	ITERABLE_TYPE(PhpLanguageLevel.PHP_7_1),
	OBJECT_TYPE(PhpLanguageLevel.PHP_7_2),
	TYPED_PROPERTIES(PhpLanguageLevel.PHP_7_4),
	ARROW_FUNCTION_SYNTAX(PhpLanguageLevel.PHP_7_4),
	COALESCE_ASSIGN(PhpLanguageLevel.PHP_7_4),
	ATTRIBUTES(PhpLanguageLevel.PHP_8_0),
	PROPERTY_PROMOTION(PhpLanguageLevel.PHP_8_0),
	UNION_TYPES(PhpLanguageLevel.PHP_8_0),
	MIXED_TYPE(PhpLanguageLevel.PHP_8_0),
	STATIC_RETURN_TYPE(PhpLanguageLevel.PHP_8_0),
	NULLSAFE_DEREFERENCING(PhpLanguageLevel.PHP_8_0),
	NAMED_ARGUMENTS(PhpLanguageLevel.PHP_8_0),
	MATCH_EXPRESSION(PhpLanguageLevel.PHP_8_0),
	ENUMS(PhpLanguageLevel.PHP_8_1),
	READONLY_PROPERTIES(PhpLanguageLevel.PHP_8_1),
	INTERSECTION_TYPES(PhpLanguageLevel.PHP_8_1),
	NEVER_TYPE(PhpLanguageLevel.PHP_8_1),
	NEW_IN_INITIALIZERS(PhpLanguageLevel.PHP_8_1),
	FIRST_CLASS_CALLABLE_SYNTAX(PhpLanguageLevel.PHP_8_1),
	READONLY_CLASSES(PhpLanguageLevel.PHP_8_2),
	DNF_TYPES(PhpLanguageLevel.PHP_8_2),
	STANDALONE_NULL_TRUE_FALSE_TYPES(PhpLanguageLevel.PHP_8_2),
	CONSTANTS_IN_TRAITS(PhpLanguageLevel.PHP_8_2),
	TYPED_CLASS_CONSTANTS(PhpLanguageLevel.PHP_8_3),
	DYNAMIC_CLASS_CONSTANT_FETCH(PhpLanguageLevel.PHP_8_3),
	PROPERTY_HOOKS(PhpLanguageLevel.PHP_8_4),
	ASYMMETRIC_VISIBILITY(PhpLanguageLevel.PHP_8_4),
	NEW_WITHOUT_PARENTHESES(PhpLanguageLevel.PHP_8_4),
	PIPE_OPERATOR(PhpLanguageLevel.PHP_8_5),
	VOID_CAST(PhpLanguageLevel.PHP_8_5);

	private final PhpLanguageLevel myMinimumLevel;

	PhpLanguageFeature(PhpLanguageLevel minimumLevel)
	{
		myMinimumLevel = minimumLevel;
	}

	@Nonnull
	public PhpLanguageLevel getMinimumLevel()
	{
		return myMinimumLevel;
	}
}
